package com.example.time2watch;

public class Forecast {
    private String date;
    private String minTemp;
    private String maxTemp;
    private String weatherCondition;

    public Forecast(String date, String minTemp, String maxTemp, String weatherCondition) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherCondition = weatherCondition;
    }


    public String getDate() {
        return date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getWeatherIconCode() {
        return "01d";
    }
}
