package com.abarza.stormy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
          if (response.isSuccessful()) {
            Log.v(TAG, response.body().string());
          }
        } catch (IOException e) {
          Log.d(TAG, "Exception caught: ", e);
        }
      }
    });

    Log.d(TAG, "Main UI CODE IS RUNNING");

  }
}
