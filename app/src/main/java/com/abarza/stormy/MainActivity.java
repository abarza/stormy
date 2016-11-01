package com.abarza.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private CurrentWeather mCurrentWeather;

  @BindView(R.id.time_label) TextView mTimeLabel;
  @BindView(R.id.temperature_label) TextView mTemperatureLabel;
  @BindView(R.id.humidity_value) TextView mHumidityValue;
  @BindView(R.id.precipitation_value) TextView mPrecipValue;
  @BindView(R.id.summary_label) TextView mSummaryLabel;
  @BindView(R.id.icon_imageView) ImageView mIconImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);


    String apiKey = "188141e178bcf497887adfc53fa8d44f";
    double latitude = 37.8267;
    double longitude = -122.4233;

    String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," +
        longitude;

    if (isNetworkAvailable()) {
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
          .url(forecastURL)
          .build();

      Call call = client.newCall(request);

      call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          try {
            String jsonData = response.body().string();
            Log.d(TAG, jsonData);
            if (response.isSuccessful()) {
              Log.d(TAG, "Connected successfully");
              mCurrentWeather = getCurrentDetails(jsonData);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  updateDisplay();
                }
              });


            } else {
              alertUserAboutError();
            }
          } catch (IOException e) {
            Log.d(TAG, "Exception caught: ", e);
          } catch (JSONException e) {
            Log.d(TAG, "Exception caught: ", e);
          }

        }
      });
    } else {
      Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }

    Log.d(TAG, "Main UI CODE IS RUNNING");

  }

  private void updateDisplay() {
    Double humidity = mCurrentWeather.getHumidity();
    Double precipChance = mCurrentWeather.getPrecipChance();
    Long time = mCurrentWeather.getTime();
    String icon = mCurrentWeather.getIcon();
    String summary = mCurrentWeather.getSummary();
    Double temperature = mCurrentWeather.getTemperature();

    Drawable drawable = ContextCompat.getDrawable(this, mCurrentWeather.getIconId());

    Log.i(TAG, "Humidity: " + humidity.toString());
    Log.i(TAG, "Time: " + time.toString());
    Log.i(TAG, "Icon: " + icon);
    Log.i(TAG, "PrecipChance: " + precipChance.toString());
    Log.i(TAG, "summary: " + summary);
    Log.i(TAG, "Formatted time: " + mCurrentWeather.getFormattedTime());
    Log.i(TAG, "Temperature: " + temperature + "ºF");
    Log.i(TAG, "Temperature: " + mCurrentWeather.getTemperatureAsInt() + "ºF");
    Log.i(TAG, "Temperature: " + mCurrentWeather.getTemperatureAsCelcius() + "ºC");
    Log.i(TAG, "Precipitation Percent: " + mCurrentWeather.doubleToPercent(precipChance));
    Log.i(TAG, "Humidity percent: " + mCurrentWeather.doubleToPercent(humidity));

    mTemperatureLabel.setText(String.valueOf(mCurrentWeather.getTemperatureAsCelcius()));
    mTimeLabel.setText(String.valueOf(mCurrentWeather.getFormattedTime()));
    mHumidityValue.setText(mCurrentWeather.doubleToPercent(humidity));
    mPrecipValue.setText(mCurrentWeather.doubleToPercent(precipChance));
    mSummaryLabel.setText(summary);git
    mIconImageView.setImageDrawable(drawable);
  }

  private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {

    JSONObject forecast = new JSONObject(jsonData);

    JSONObject currently = forecast.getJSONObject("currently");
    String TimeZone = forecast.getString("timezone");

    CurrentWeather currentWeather = new CurrentWeather();
    currentWeather.setHumidity(currently.getDouble("humidity"));
    currentWeather.setTime(currently.getLong("time"));
    currentWeather.setIcon(currently.getString("icon"));
    currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
    currentWeather.setSummary(currently.getString("summary"));
    currentWeather.setTimezone(TimeZone);
    currentWeather.setTemperature(currently.getDouble("temperature"));

    Log.i(TAG, "From JSON: " + TimeZone);
    Log.i(TAG, "Currently" + currently);


    return currentWeather;


  }

  private boolean isNetworkAvailable() {
    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

    boolean isAvailable = false;
    if (networkInfo != null && networkInfo.isConnected()) {
      isAvailable = true;
    }
    return isAvailable;
  }

  private void alertUserAboutError() {
    AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getSupportFragmentManager(), "error_dialog");
  }
}
