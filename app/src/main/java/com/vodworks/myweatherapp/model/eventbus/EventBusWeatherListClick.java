package com.vodworks.myweatherapp.model.eventbus;

import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.ArrayList;
import java.util.List;

public class EventBusWeatherListClick {

    private int position, parentIndex;
    private ArrayList<WeatherEntity> weatherEntityList;

    public EventBusWeatherListClick(int position, int parentIndex, ArrayList<WeatherEntity> weatherEntityList) {
        this.position = position;
        this.parentIndex = parentIndex;
        this.weatherEntityList = weatherEntityList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public ArrayList<WeatherEntity> getWeatherEntityList() {
        return weatherEntityList;
    }

    public void setWeatherEntityList(ArrayList<WeatherEntity> weatherEntityList) {
        this.weatherEntityList = weatherEntityList;
    }
}
