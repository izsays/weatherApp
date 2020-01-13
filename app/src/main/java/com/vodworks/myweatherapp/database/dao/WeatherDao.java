package com.vodworks.myweatherapp.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert
    void insertNewWeather(WeatherEntity weatherEntity);

    @Query("SELECT * FROM WeatherEntity")
    List<WeatherEntity> getWeatherList();

    @Query("SELECT * FROM WeatherEntity WHERE id = :id")
    WeatherEntity getWeatherData(int id);

    @Delete
    void deleteWeatherData(WeatherEntity weatherEntity);

    @Query("SELECT * FROM WeatherEntity")
    Cursor selectAll();

    @Query("SELECT * FROM WeatherEntity WHERE id = :id")
    Cursor selectById(long id);

    @Insert
    long insert(WeatherEntity weatherEntity);

}
