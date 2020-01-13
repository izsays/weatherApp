package com.vodworks.myweatherapp.retrofit;

import com.vodworks.myweatherapp.model.retrofitresponse.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApis {

    @GET("weather")
    Call<CurrentWeather> getCurrentWeather(@Query("APPID") String appId,
                                           @Query("lat") String latitude,
                                           @Query("lon") String longitude);

    @GET("weather")
    Call<CurrentWeather> searchByName(@Query("APPID") String appId,
                                      @Query("q") String query);

}