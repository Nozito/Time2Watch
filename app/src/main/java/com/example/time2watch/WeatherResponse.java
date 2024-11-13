package com.example.time2watch;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("name")
    private String name;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    // Getters
    public String getName() {
        return name != null ? name : "";
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    // Classe interne pour les données principales (températures)
    public static class Main {

        @SerializedName("temp")
        private float temp;

        @SerializedName("temp_min")
        private float tempMin;

        @SerializedName("temp_max")
        private float tempMax;

        // Retourne la température principale (arrondie à un entier)
        public int getTemp() {
            return Math.round(temp);
        }

        // Retourne la température minimale
        public float getTempMin() {
            return tempMin;
        }

        // Retourne la température maximale
        public float getTempMax() {
            return tempMax;
        }
    }

    // Classe interne pour les informations météo spécifiques
    public static class Weather {

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        // Retourne la description (texte)
        public String getDescription() {
            return description != null ? description : ""; // Évite les retours null
        }

        // Retourne le code de l'icône
        public String getIcon() {
            return icon != null ? icon : ""; // Évite les retours null
        }
    }
}