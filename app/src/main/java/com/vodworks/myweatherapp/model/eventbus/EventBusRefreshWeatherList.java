package com.vodworks.myweatherapp.model.eventbus;


import com.vodworks.myweatherapp.database.entities.WeatherEntity;

public class EventBusRefreshWeatherList {

    private WeatherEntity weatherEntity;

    public EventBusRefreshWeatherList(WeatherEntity weatherEntity) {
        this.weatherEntity = weatherEntity;
    }

    public WeatherEntity getWeatherEntity() {
        return weatherEntity;
    }

    public void setWeatherEntity(WeatherEntity weatherEntity) {
        this.weatherEntity = weatherEntity;
    }
}
