package com.vodworks.myweatherapp.backgroundwork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.common.Constants;
import com.vodworks.myweatherapp.database.DatabaseClient;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;
import com.vodworks.myweatherapp.model.eventbus.EventBusRefreshWeatherList;
import com.vodworks.myweatherapp.model.retrofitresponse.CurrentWeather;
import com.vodworks.myweatherapp.retrofit.ApiClient;
import com.vodworks.myweatherapp.retrofit.WeatherApis;
import com.vodworks.myweatherapp.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetWeatherDataWork extends Worker {

    //    Retrofit fields...
    private Call<CurrentWeather> currentWeatherCall;

    //    Android fields....
    private Context context;

    //    Instance fields....
    private double latitude, longitude;

    public GetWeatherDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            latitude = PrefUtils.getDoubleFromPref(context, Constants.PrefConstants.LATITUDE, 0.0);
            longitude = PrefUtils.getDoubleFromPref(context, Constants.PrefConstants.LONGITUDE, 0.0);
            if (latitude != 0.0 && longitude != 0.0)
                getWeatherData(latitude, longitude);
            startNewRequest();
            return Result.success();
        } catch (Exception e) {
            startNewRequest();
            e.printStackTrace();
            return Result.failure();
        }
    }

    private void startNewRequest() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest weatherDataWork = new OneTimeWorkRequest.Builder(GetWeatherDataWork.class)
                .setInitialDelay(Constants.WORKER_TIME_INTERVAL, TimeUnit.MINUTES).addTag("weather_data")
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(weatherDataWork);
    }

    private void getWeatherData(double latitude, double longitude) {
        currentWeatherCall = ApiClient.getClient().create(WeatherApis.class)
                .getCurrentWeather(Constants.RetrofitConstants.WEATHER_MAP_API_KEY, String.valueOf(latitude), String.valueOf(longitude));
        currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if (response != null && response.isSuccessful()) {

                    CurrentWeather currentWeather = response.body();
                    if (currentWeather != null) {

                        @SuppressLint("SimpleDateFormat") String timestamp = new SimpleDateFormat(Constants.TIME_FORMAT).format(new Date());
                        int iconId = R.drawable.ic_action_dunno;
                        String condition = "";
                        if (currentWeather.getWeather() != null && currentWeather.getWeather().size() > 0) {
                            iconId = currentWeather.getWeather().get(0).getId();
                            condition = currentWeather.getWeather().get(0).getMain();
                        }

                        WeatherEntity weatherEntity = new WeatherEntity(timestamp,
                                latitude, longitude, currentWeather.getName(), currentWeather.getMain().getTemp(),
                                currentWeather.getSys().getCountry(), iconId, condition, currentWeather.getWind().getSpeed(),
                                currentWeather.getMain().getPressure(), currentWeather.getMain().getTempMax(), currentWeather.getMain().getTempMin());
                        new GetWeatherDataWork.InsertWeatherTask(context).execute(weatherEntity);
                    }

                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    class InsertWeatherTask extends AsyncTask<WeatherEntity, Void, WeatherEntity> {

        Context context;

        InsertWeatherTask(Context context) {
            this.context = context;
        }

        @Override
        protected WeatherEntity doInBackground(WeatherEntity... weatherEntities) {
            DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao()
                    .insertNewWeather(weatherEntities[0]);
            return weatherEntities[0];
        }

        @Override
        protected void onPostExecute(WeatherEntity aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new EventBusRefreshWeatherList(aVoid));
        }
    }

}