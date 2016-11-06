package com.abarza.stormy.weather;

/**
 * Created by abarza on 05-11-16.
 */

public class Hour {

  private long mTime;
  private String mSummary;
  private double mTemperature;
  private String mIcon;
  private String mTimeZone;

  public long getTime() {
    return mTime;
  }

  public void setTime(long time) {
    mTime = time;
  }

  public String getSummary() {
    return mSummary;
  }

  public void setSummary(String summary) {
    mSummary = summary;
  }

  public double getTemperature() {
    return mTemperature;
  }

  public void setTemperature(double temperature) {
    mTemperature = temperature;
  }

  public String getIcon() {
    return mIcon;
  }

  public void setIcon(String icon) {
    mIcon = icon;
  }

  public String getTimeZone() {
    return mTimeZone;
  }

  public void setTimeZone(String timeZone) {
    mTimeZone = timeZone;
  }
}
