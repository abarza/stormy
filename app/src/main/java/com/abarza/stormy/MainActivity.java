package com.abarza.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


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
            Log.d(TAG, response.body().string());
            if (response.isSuccessful()) {
              Log.d(TAG, "Connected successfully");

            } else {
              alertUserAboutError();
            }
          } catch (IOException e) {
            Log.d(TAG, "Exception caught: ", e);
          }
        }
      });
    } else {
      Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }

    Log.d(TAG, "Main UI CODE IS RUNNING");

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
