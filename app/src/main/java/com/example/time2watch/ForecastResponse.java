package com.example.time2watch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastResponse {

    private long dt;
    private Main main;
    private List<Weather> weather;
    private List<WeatherForecast> list;

    public long getDt() {
        return dt;
    }

    public int getMinTemp() {
        return main != null ? Math.round(main.getTempMin()) : 0;
    }

    public int getMaxTemp() {
        return main != null ? Math.round(main.getTempMax()) : 0;
    }

    public String getWeatherIconCode() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : "";
    }

    public String getWeatherDescription() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : "";
    }

    public List<WeatherForecast> getList() {
        return list;
    }

    public static class Main {
        float tempMin;
        float tempMax;

        public float getTempMin() {
            return tempMin;
        }

        public float getTempMax() {
            return tempMax;
        }
    }

    public static class Weather {
        private String description;
        private String icon;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class WeatherForecast {
        private long dt;
        Main main;
        private List<Weather> weather;

        public String getDate() {
            Date dateObj = new Date(dt * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(dateObj);
        }

        public int getMinTemp() {
            return main != null ? Math.round(main.getTempMin()) : 0;
        }

        public int getMaxTemp() {
            return main != null ? Math.round(main.getTempMax()) : 0;
        }

        public String getWeatherIconCode() {
            return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : "";
        }

        public String getWeatherDescription() {
            return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : "";
        }

        // Getter pour le timestamp
        public long getDt() {
            return dt;
        }
    }
}