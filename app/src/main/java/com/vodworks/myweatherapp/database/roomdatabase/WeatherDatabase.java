package com.vodworks.myweatherapp.database.roomdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vodworks.myweatherapp.database.dao.WeatherDao;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;

@Database(entities = {WeatherEntity.class}, version = 3, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}