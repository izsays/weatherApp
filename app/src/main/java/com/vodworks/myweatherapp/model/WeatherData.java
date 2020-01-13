package com.vodworks.myweatherapp.model;

import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.ArrayList;

public class WeatherData {

    private String cityName;
    private ArrayList<WeatherEntity> weatherEntityList;

    public WeatherData(String cityName, ArrayList<WeatherEntity> weatherEntityList) {
        this.cityName = cityName;
        this.weatherEntityList = weatherEntityList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<WeatherEntity> getWeatherEntityList() {
        return weatherEntityList;
    }

    public void setWeatherEntityList(ArrayList<WeatherEntity> weatherEntityList) {
        this.weatherEntityList = weatherEntityList;
    }
}
