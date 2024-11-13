package com.example.time2watch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastResponse.WeatherForecast> forecastList;

    public ForecastAdapter(List<ForecastResponse.WeatherForecast> forecastList) {
        this.forecastList = filterAndSortDailyForecasts(forecastList);  // Filtrer et trier les prévisions
    }

    // Méthode pour regrouper les prévisions par jour, calculer les températures min/max et les trier chronologiquement
    private List<ForecastResponse.WeatherForecast> filterAndSortDailyForecasts(List<ForecastResponse.WeatherForecast> forecastList) {
        Map<String, ForecastResponse.WeatherForecast> dailyForecasts = new HashMap<>();

        for (ForecastResponse.WeatherForecast forecast : forecastList) {
            String date = forecast.getDate();

            if (!dailyForecasts.containsKey(date)) {
                dailyForecasts.put(date, forecast);
            } else {
                ForecastResponse.WeatherForecast existingForecast = dailyForecasts.get(date);
                if (forecast.getMinTemp() < existingForecast.getMinTemp()) {
                    existingForecast.main.tempMin = forecast.main.tempMin;
                }
                if (forecast.getMaxTemp() > existingForecast.getMaxTemp()) {
                    existingForecast.main.tempMax = forecast.main.tempMax;
                }
            }
        }

        List<ForecastResponse.WeatherForecast> sortedList = new ArrayList<>(dailyForecasts.values());

        sortedList.sort((forecast1, forecast2) -> Long.compare(forecast1.getDt(), forecast2.getDt()));

        return sortedList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout pour chaque item de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.WeatherForecast forecast = forecastList.get(position);

        if (forecast == null || forecast.getWeatherIconCode() == null || forecast.getWeatherIconCode().isEmpty()) {
            return;
        }

        long timestamp = forecast.getDt();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);
        String dateString = sdf.format(new Date(timestamp * 1000)); // Conversion du timestamp
        holder.date.setText(dateString);

        holder.minTemp.setText("Min : " + Math.round(forecast.getMinTemp()) + "°C");
        holder.maxTemp.setText("Max : " + Math.round(forecast.getMaxTemp()) + "°C");

        String iconCode = forecast.getWeatherIconCode();
        switch (iconCode) {
            case "01d":
            case "01n":
                holder.iconImageView.setImageResource(R.drawable.ic_sun);
                break;
            case "02d":
            case "02n":
                holder.iconImageView.setImageResource(R.drawable.ic_few_clouds);
                break;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                holder.iconImageView.setImageResource(R.drawable.ic_clouds);
                break;
            case "09d":
            case "09n":
                holder.iconImageView.setImageResource(R.drawable.ic_shower_rain);
                break;
            case "10d":
            case "10n":
                holder.iconImageView.setImageResource(R.drawable.ic_rain);
                break;
            case "11d":
            case "11n":
                holder.iconImageView.setImageResource(R.drawable.ic_storm);
                break;
            case "13d":
            case "13n":
                holder.iconImageView.setImageResource(R.drawable.ic_snow);
                break;
            case "50d":
            case "50n":
                holder.iconImageView.setImageResource(R.drawable.ic_mist);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return forecastList != null ? forecastList.size() : 0;
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        public ImageView iconImageView;
        TextView date, minTemp, maxTemp;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.forecast_date);
            minTemp = itemView.findViewById(R.id.forecast_min_temp);
            maxTemp = itemView.findViewById(R.id.forecast_max_temp);
            iconImageView = itemView.findViewById(R.id.forecast_weather_icon);
        }
    }
}