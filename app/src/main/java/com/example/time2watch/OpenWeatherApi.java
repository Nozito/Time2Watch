package com.example.time2watch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {
    String API_KEY = "945eade533577498c1645b72a3a7b104";

    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String city, @Query("appid") String apiKey, @Query("units") String units);

    @GET("forecast")
    Call<ForecastResponse> getFiveDayForecast(@Query("q") String city, @Query("appid") String apiKey, @Query("units") String units);
}