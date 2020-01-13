package com.vodworks.myweatherapp.utils;

public class WeatherIconUtils {

    public static String getWeatherIconAnimation(int id) {
        if (id > -1 && id < 301) {
            return "storm.json";
        } else if (id > 300 && id < 501) {
            return "light_rain.json";
        } else if (id > 500 && id < 601) {
            return "shower.json";
        } else if (id > 600 && id < 701 || id == 903) {
            return "snow.json";
        } else if (id > 700 && id < 772) {
            return "fog.json";
        } else if (id > 771 && id < 800) {
            return "storm.json";
        } else if (id == 800 || id == 904) {
            return "sunny.json";
        } else if (id > 800 && id < 805) {
            return "cloudy.json";
        } else if (id > 899 && id < 902 || id > 904 && id < 1001) {
            return "storm.json";
        } else {
            return "sunny.json";
        }
    }

}
