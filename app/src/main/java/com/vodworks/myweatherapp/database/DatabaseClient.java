package com.vodworks.myweatherapp.database;

import android.content.Context;

import androidx.room.Room;

import com.vodworks.myweatherapp.database.roomdatabase.WeatherDatabase;

public class DatabaseClient {

    private static DatabaseClient mInstance;

    private WeatherDatabase weatherDatabase;

    private DatabaseClient(Context mCtx) {
        weatherDatabase = Room.databaseBuilder(mCtx, WeatherDatabase.class, "WeatherDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getDatabaseClient(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public WeatherDatabase getWeatherDatabase() {
        return weatherDatabase;
    }
}
