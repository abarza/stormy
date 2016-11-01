package com.abarza.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by abarza on 31-10-16.
 */

public class CurrentWeather {
  private String mIcon;
  private long mTime;
  private double mTemperature;
  private double mHumidity;
  private double mPrecipChance;
  private String mTimezone;

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

  public long getTime() {
    return mTime;
  }

  public String getFormattedTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
    formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
    Date dateTime = new Date(getTime() * 1000);
    String timeString = formatter.format(dateTime);

    return  timeString;
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
    int temperatureAsInt;
    return temperatureAsInt = (int) Math.round(getTemperature());

  }

  public int getTemperatureAsCelcius() {
    int temperatureAsCelcius;
    return temperatureAsCelcius = ((getTemperatureAsInt() - 32) * 5)/9;
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

  private String mSummary;
}
