package com.example.time2watch;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeteoActivity extends AppCompatActivity {

    private TextView cityName, temperature, weatherDescription, tempMin, tempMax, forecastTitle;
    private RecyclerView recyclerView;
    private OpenWeatherApi api;
    private ForecastAdapter forecastAdapter;

    private Button btnWeather, btnHome, btnMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLocale("fr", "FR");

        setContentView(R.layout.activity_meteo);

        cityName = findViewById(R.id.city_name);
        temperature = findViewById(R.id.temperature);
        weatherDescription = findViewById(R.id.weather_description);
        tempMin = findViewById(R.id.temp_min);
        tempMax = findViewById(R.id.temp_max);
        forecastTitle = findViewById(R.id.forecast_title);
        recyclerView = findViewById(R.id.recycler_view_forecast);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnWeather = findViewById(R.id.btn_weather);
        btnHome = findViewById(R.id.btn_home);
        btnMovies = findViewById(R.id.btn_movies);

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeteoActivity.this, "Météo actuelle", Toast.LENGTH_SHORT).show();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeteoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeteoActivity.this, SuggestionsActivity.class);
                startActivity(intent);
            }
        });

        setupRetrofit();
        getWeatherData("Paris");
    }

    private void setLocale(String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    private void setupRetrofit() {
        // Configuration de Retrofit pour l'API
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(OpenWeatherApi.class);
    }

    private void getWeatherData(String city) {
        Call<WeatherResponse> weatherCall = api.getCurrentWeather(city, OpenWeatherApi.API_KEY, "metric");
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    cityName.setText(weatherResponse.getName());
                    temperature.setText(weatherResponse.getMain().getTemp() + "°");
                    weatherDescription.setText(weatherResponse.getWeather().get(0).getDescription());
                    tempMin.setText("Min : " + weatherResponse.getMain().getTempMin() + "°");
                    tempMax.setText("Max : " + weatherResponse.getMain().getTempMax() + "°");

                    getForecastData(city);
                } else {
                    showErrorToast("Erreur de récupération des données météo");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                showErrorToast("Échec de la connexion API");
            }
        });
    }

    private void getForecastData(String city) {
        Call<ForecastResponse> forecastCall = api.getFiveDayForecast(city, OpenWeatherApi.API_KEY, "metric");
        forecastCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, retrofit2.Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastResponse.WeatherForecast> forecasts = response.body().getList();

                    forecastAdapter = new ForecastAdapter(forecasts);
                    recyclerView.setAdapter(forecastAdapter);
                } else {
                    showErrorToast("Erreur de récupération des prévisions météo");
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                showErrorToast("Échec de la connexion pour les prévisions météo");
            }
        });
    }

    private void showErrorToast(String message) {
        Toast.makeText(MeteoActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void sendWeatherCondition() {
        String weatherCondition = "clear";
        Intent intent = new Intent(MeteoActivity.this, SuggestionsActivity.class);

        if (weatherCondition != null && !weatherCondition.isEmpty()) {
            intent.putExtra("weather_condition", weatherCondition);
            startActivity(intent);
        } else {
            Log.e("MeteoActivity", "La condition météo est nulle ou vide.");
        }
    }
}