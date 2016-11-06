package com.abarza.stormy.weather;

import com.abarza.stormy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by abarza on 31-10-16.
 */

public class Current {
  private String mIcon;
  private long mTime;
  private double mTemperature;
  private double mHumidity;
  private double mPrecipChance;
  private String mTimezone;
  private String mSummary;
  private int iconId;

  public String getTimezone() {
    return mTimezone;
  }

  public void setTimezone(String timezone) {
    mTimezone = timezone;
  }

  public String getIcon() {
    return mIcon;
  }

  public void setIcon(String icon) {
    mIcon = icon;
  }

  public int getIconId() {

    int iconId;

    switch (mIcon) {
      case "clear-day":
        iconId = R.drawable.clear_day;
        break;
      case "clear-night":
        iconId = R.drawable.clear_day;
        break;
      case "rain":
        iconId = R.drawable.rain;
        break;
      case "snow":
        iconId = R.drawable.snow;
        break;
      case "sleet":
        iconId = R.drawable.sleet;
        break;
      case "wind":
        iconId = R.drawable.wind;
        break;
      case "fog":
        iconId = R.drawable.fog;
        break;
      case "cloudy":
        iconId = R.drawable.cloudy;
        break;
      case "partly-cloudy-day":
        iconId = R.drawable.partly_cloudy;
        break;
      case "partly-cloudy-night":
        iconId = R.drawable.cloudy_night;
        break;
      default:
        iconId = R.drawable.clear_day;
        break;
    }

    return iconId;
  }

  public long getTime() {
    return mTime;
  }

  public String getFormattedTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
    formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
    Date dateTime = new Date(getTime() * 1000);
    String timeString = formatter.format(dateTime);
    return timeString;
  }

  public void setTime(long time) {
    mTime = time;
  }

  public double getTemperature() {
    return mTemperature;
  }

  public void setTemperature(double temperature) {
    mTemperature = temperature;
  }

  public int getTemperatureAsInt() {
    int temperatureAsInt = (int) Math.round(getTemperature());
    return temperatureAsInt;

  }

  public int getTemperatureAsCelcius() {
    int temperatureAsCelcius = ((getTemperatureAsInt() - 32) * 5) / 9;
    return temperatureAsCelcius;
  }

  public double getHumidity() {
    return mHumidity;
  }

  public void setHumidity(double humidity) {
    mHumidity = humidity;
  }

  public double getPrecipChance() {
    return mPrecipChance;
  }

  public void setPrecipChance(double precipChance) {
    mPrecipChance = precipChance;
  }

  public String getSummary() {
    return mSummary;
  }

  public void setSummary(String summary) {
    mSummary = summary;
  }

  public String doubleToPercent(Double d) {
    float percent = (float) (d * 100);
    String percentAsString = String.format("%.0f%%", percent);
    return percentAsString;
  }

}
