package com.vodworks.myweatherapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vodworks.myweatherapp.database.entities.WeatherEntity;
import com.vodworks.myweatherapp.repositories.WeatherDataRepository;

import java.util.List;

public class WeatherDataViewModel extends AndroidViewModel {

    private WeatherDataRepository weatherDataRepository;
    private LiveData<List<WeatherEntity>> weatherEntityList;

    public WeatherDataViewModel(@NonNull Application application) {
        super(application);

        weatherDataRepository = new WeatherDataRepository(application);
        weatherEntityList = weatherDataRepository.getWeatherEntityList();

    }

    public LiveData<List<WeatherEntity>> getWeatherEntityList() {
        return weatherEntityList;
    }

    public void saveWeatherData(WeatherEntity weatherEntity) {
        weatherDataRepository.saveWeatherData(weatherEntity);
    }

    public void deleteWeatherData(WeatherEntity weatherEntity) {
        weatherDataRepository.deleteWeatherData(weatherEntity);
    }

}