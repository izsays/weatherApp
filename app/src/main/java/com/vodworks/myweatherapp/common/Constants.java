package com.vodworks.myweatherapp.common;

public interface Constants {

    //    Time in minutes....
    int WORKER_TIME_INTERVAL = 10;
    String TIME_FORMAT = "dd MMMM, yyyy  HH:mm:ss";

    interface PrefConstants {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    interface RetrofitConstants {
        String WEATHER_MAP_API_BASE_URL = "http://api.openweathermap.org/data/2.5/";
        String WEATHER_MAP_API_KEY = "ae78e0136a2b02d160dec8923db47b37";
    }

    interface BundleConstants {
        String WEATHER_DATA_LIST = "weather_data_list";
        String CLICKED_INDEX = "clicked_index";
    }

}