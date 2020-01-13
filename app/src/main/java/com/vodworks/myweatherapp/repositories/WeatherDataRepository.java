package com.vodworks.myweatherapp.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vodworks.myweatherapp.database.DatabaseClient;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.List;

public class WeatherDataRepository {

    private DatabaseClient databaseClient;
    private LiveData<List<WeatherEntity>> weatherEntityList;

    public WeatherDataRepository(Context context) {
        databaseClient = DatabaseClient.getDatabaseClient(context.getApplicationContext());
//        weatherEntityList = databaseClient.getWeatherDatabase().getWeatherDao().getWeatherList();
    }

    public LiveData<List<WeatherEntity>> getWeatherEntityList() {
        return weatherEntityList;
    }

    public void saveWeatherData(WeatherEntity weatherEntity) {
        new InsertWeatherTask().execute(weatherEntity);
    }

    public void deleteWeatherData(WeatherEntity weatherEntity) {
        new DeleteWeatherDataTask().execute(weatherEntity);
    }

    class InsertWeatherTask extends AsyncTask<WeatherEntity, Void, Void> {

        @Override
        protected Void doInBackground(WeatherEntity... weatherEntities) {
            databaseClient.getWeatherDatabase().getWeatherDao().insertNewWeather(weatherEntities[0]);
            return null;
        }

    }

    class DeleteWeatherDataTask extends AsyncTask<WeatherEntity, Void, Void> {

        @Override
        protected Void doInBackground(WeatherEntity... weatherEntities) {
            databaseClient.getWeatherDatabase().getWeatherDao()
                    .deleteWeatherData(weatherEntities[0]);
            return null;
        }

    }

}