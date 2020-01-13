package com.vodworks.myweatherapp.model.eventbus;

public class EventBusSearchCity {

    private String cityName;

    public EventBusSearchCity(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
