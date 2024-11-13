package com.example.time2watch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastResponse {

    private long dt;  // Timestamp UNIX (en secondes)
    private Main main;
    private List<Weather> weather;
    private List<WeatherForecast> list; // Liste des prévisions météo

    // Getter pour le timestamp
    public long getDt() {
        return dt;
    }

    // Getter pour la température minimale
    public int getMinTemp() {
        // Vérification que 'main' est non nul avant d'accéder aux valeurs
        return main != null ? Math.round(main.getTempMin()) : 0;
    }

    // Getter pour la température maximale
    public int getMaxTemp() {
        // Vérification que 'main' est non nul avant d'accéder aux valeurs
        return main != null ? Math.round(main.getTempMax()) : 0;
    }

    // Getter pour l'icône météo (retourne l'icône du premier objet Weather dans la liste)
    public String getWeatherIconCode() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : "";
    }

    // Getter pour la description de la météo (retourne la description du premier objet Weather)
    public String getWeatherDescription() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : "";
    }

    // Getter pour la liste des prévisions météo
    public List<WeatherForecast> getList() {
        return list;
    }

    // Classe interne pour les informations de température
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

    // Classe interne pour les informations météorologiques
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

    // Classe interne pour les prévisions météo détaillées
    public static class WeatherForecast {
        private long dt;  // Timestamp UNIX
        Main main;
        private List<Weather> weather;  // Liste des objets Weather

        // Getter pour la date sous forme lisible
        public String getDate() {
            Date dateObj = new Date(dt * 1000);  // Conversion du timestamp UNIX en millisecondes
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);  // Format de la date
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(dateObj);  // Retourne la date formatée
        }

        public int getMinTemp() {
            return main != null ? Math.round(main.getTempMin()) : 0;  // Si 'main' est null, retourne 0
        }

        public int getMaxTemp() {
            return main != null ? Math.round(main.getTempMax()) : 0;  // Si 'main' est null, retourne 0
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