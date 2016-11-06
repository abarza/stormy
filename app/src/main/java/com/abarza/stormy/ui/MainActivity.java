package com.abarza.stormy.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abarza.stormy.R;
import com.abarza.stormy.weather.Current;
import com.abarza.stormy.weather.Day;
import com.abarza.stormy.weather.Forecast;
import com.abarza.stormy.weather.Hour;

import org.json.JSONArray;
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

  private Forecast mForecast;

  @BindView(R.id.time_label) TextView mTimeLabel;
  @BindView(R.id.temperature_label) TextView mTemperatureLabel;
  @BindView(R.id.humidity_value) TextView mHumidityValue;
  @BindView(R.id.precipitation_value) TextView mPrecipValue;
  @BindView(R.id.summary_label) TextView mSummaryLabel;
  @BindView(R.id.icon_imageView) ImageView mIconImageView;
  @BindView(R.id.refresh_button) ImageButton mRefreshButton;
  @BindView(R.id.progressBar) ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mProgressBar.setVisibility(View.INVISIBLE);

    final double latitude = 37.8267;
    final double longitude = -122.4233;

    mRefreshButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getForecast(latitude, longitude);
      }
    });


    getForecast(latitude, longitude);

  }

  private void getForecast(double latitude, double longitude) {
    String apiKey = "188141e178bcf497887adfc53fa8d44f";

    String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," +
        longitude;

    if (isNetworkAvailable()) {
      toggleRefresh();
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
          .url(forecastURL)
          .build();

      Call call = client.newCall(request);

      call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });
          alertUserAboutError();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });
          try {
            String jsonData = response.body().string();
            Log.d(TAG, jsonData);
            if (response.isSuccessful()) {
              Log.d(TAG, "Connected successfully");
              mForecast = parseForecastDetails(jsonData);
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
  }

  private void toggleRefresh() {
    if (mProgressBar.getVisibility() == View.INVISIBLE){
      mProgressBar.setVisibility(View.VISIBLE);
      mRefreshButton.setVisibility(View.INVISIBLE);
    } else {
      mProgressBar.setVisibility(View.INVISIBLE);
      mRefreshButton.setVisibility(View.VISIBLE);
    }

  }

  private void updateDisplay() {
    Current current = mForecast.getCurrent();
    Double humidity = current.getHumidity();
    Double precipChance = current.getPrecipChance();
    Long time = current.getTime();
    String icon = current.getIcon();
    String summary = current.getSummary();
    Double temperature = current.getTemperature();

    Drawable drawable = ContextCompat.getDrawable(this, current.getIconId());

    Log.i(TAG, "Humidity: " + humidity.toString());
    Log.i(TAG, "Time: " + time.toString());
    Log.i(TAG, "Icon: " + icon);
    Log.i(TAG, "PrecipChance: " + precipChance.toString());
    Log.i(TAG, "summary: " + summary);
    Log.i(TAG, "Formatted time: " + current.getFormattedTime());
    Log.i(TAG, "Temperature: " + temperature + "ºF");
    Log.i(TAG, "Temperature: " + current.getTemperatureAsInt() + "ºF");
    Log.i(TAG, "Temperature: " + current.getTemperatureAsCelcius() + "ºC");
    Log.i(TAG, "Precipitation Percent: " + current.doubleToPercent(precipChance));
    Log.i(TAG, "Humidity percent: " + current.doubleToPercent(humidity));

    mTemperatureLabel.setText(String.valueOf(current.getTemperatureAsCelcius()));
    mTimeLabel.setText(String.valueOf(current.getFormattedTime()));
    mHumidityValue.setText(current.doubleToPercent(humidity));
    mPrecipValue.setText(current.doubleToPercent(precipChance));
    mSummaryLabel.setText(summary);
    mIconImageView.setImageDrawable(drawable);
  }

  private Forecast parseForecastDetails(String jsonData) throws JSONException {

    Forecast forecast = new Forecast();

    forecast.setCurrent(getCurrentDetails(jsonData));

    forecast.setHourlyForecast(getHourlyForecast(jsonData));
    forecast.setDailyForecast(getDailyForecast(jsonData));


    return forecast;

  }

  private Day[] getDailyForecast(String jsonData) throws JSONException {
    JSONObject forecast = new JSONObject(jsonData);
    String timeZone = forecast.getString("timezone");
    JSONObject daily = forecast.getJSONObject("daily");
    JSONArray data = daily.getJSONArray("data");

    Day[] days = new Day[data.length()];

    for (int i = 0; i < data.length(); i++) {
      JSONObject jsonDay = data.getJSONObject(i);
      Day day = new Day();

      day.setTimeZone(timeZone);
      day.setTime(jsonDay.getLong("time"));
      day.setIcon(jsonDay.getString("icon"));
      day.setSummary(jsonDay.getString("summary"));
      day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));

      days[i] = day;
    }

    return days;


  }

  private Hour[] getHourlyForecast(String jsonData) throws JSONException {
    JSONObject forecast = new JSONObject(jsonData);
    String timeZone = forecast.getString("timezone");
    JSONObject hourly = forecast.getJSONObject("hourly");
    JSONArray data = hourly.getJSONArray("data");

    Hour[] hours = new Hour[data.length()];

    for (int i = 0 ; i < data.length(); i++) {
      JSONObject jsonHour = data.getJSONObject(i);
      Hour hour = new Hour();

      hour.setSummary(jsonHour.getString("summary"));
      hour.setTemperature(jsonHour.getDouble("temperature"));
      hour.setIcon(jsonHour.getString("icon"));
      hour.setTime(jsonHour.getLong("time"));
      hour.setTimeZone(timeZone);

      hours[i] = hour;
    }

    return hours;

  }


  private Current getCurrentDetails(String jsonData) throws JSONException {

    JSONObject forecast = new JSONObject(jsonData);

    JSONObject currently = forecast.getJSONObject("currently");
    String TimeZone = forecast.getString("timezone");

    Current current = new Current();
    current.setHumidity(currently.getDouble("humidity"));
    current.setTime(currently.getLong("time"));
    current.setIcon(currently.getString("icon"));
    current.setPrecipChance(currently.getDouble("precipProbability"));
    current.setSummary(currently.getString("summary"));
    current.setTimezone(TimeZone);
    current.setTemperature(currently.getDouble("temperature"));

    Log.i(TAG, "From JSON: " + TimeZone);
    Log.i(TAG, "Currently" + currently);


    return current;


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
