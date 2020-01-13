package com.vodworks.myweatherapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.adapters.WeatherDataMainListPagerAdapter;
import com.vodworks.myweatherapp.backgroundwork.GetWeatherDataWork;
import com.vodworks.myweatherapp.common.Constants;
import com.vodworks.myweatherapp.database.DatabaseClient;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;
import com.vodworks.myweatherapp.model.WeatherData;
import com.vodworks.myweatherapp.model.eventbus.EventBusAddNewData;
import com.vodworks.myweatherapp.model.eventbus.EventBusLongClick;
import com.vodworks.myweatherapp.model.eventbus.EventBusNewLocation;
import com.vodworks.myweatherapp.model.eventbus.EventBusRefreshWeatherList;
import com.vodworks.myweatherapp.model.eventbus.EventBusSearchCity;
import com.vodworks.myweatherapp.model.retrofitresponse.CurrentWeather;
import com.vodworks.myweatherapp.retrofit.ApiClient;
import com.vodworks.myweatherapp.retrofit.WeatherApis;
import com.vodworks.myweatherapp.utils.NetworkUtils;
import com.vodworks.myweatherapp.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherListFragment extends Fragment {

    //    Android fields...
    private Context context;
    @BindView(R.id.viewPagerWeatherList)
    ViewPager viewPagerMain;

    //    Instance fields....
    private boolean isLocationFirstTime;

    //    Adapter fields....
    private List<WeatherData> weatherDataList = new ArrayList<>();
    private WeatherDataMainListPagerAdapter weatherDataMainListPagerAdapter;

    //    Retrofit fields....
    private Call<CurrentWeather> currentWeatherCall;

    public WeatherListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        context = getContext();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        Getting weather data from room....
        new GetWeatherDataList(context).execute();

        //        Setting work manager....
        WorkManager.getInstance().cancelAllWorkByTag("weather_data");
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        OneTimeWorkRequest weatherDataWork = new OneTimeWorkRequest.Builder(GetWeatherDataWork.class)
                .setInitialDelay(Constants.WORKER_TIME_INTERVAL, TimeUnit.MINUTES).addTag("weather_data")
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(weatherDataWork);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (currentWeatherCall != null && currentWeatherCall.isExecuted()) {
            currentWeatherCall.cancel();
        }
    }

    //    All event bus methods....
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNewLocation(EventBusNewLocation eventBusRefreshLocation) {
        PrefUtils.saveToPrefs(context, Constants.PrefConstants.LATITUDE, eventBusRefreshLocation.getLatitude());
        PrefUtils.saveToPrefs(context, Constants.PrefConstants.LONGITUDE, eventBusRefreshLocation.getLongitude());
        if (!isLocationFirstTime && NetworkUtils.isNetworkAvailable(context)) {
            getWeatherData(eventBusRefreshLocation.getLatitude(), eventBusRefreshLocation.getLongitude());
        }
        isLocationFirstTime = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusLongPressed(EventBusLongClick eventBusLongClick) {
        PopupMenu popup = new PopupMenu(context, eventBusLongClick.getView());
        popup.getMenuInflater().inflate(R.menu.menu_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    new DeleteWeatherDataTask(context, eventBusLongClick.getPosition())
                            .execute(weatherDataList.get(eventBusLongClick.getParentIndex()).getWeatherEntityList().get(eventBusLongClick.getPosition()));
                    return true;
            }
            return true;
        });
        popup.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusRefreshClicked(EventBusAddNewData eventBusAddNewData) {
        if (weatherDataList.get(eventBusAddNewData.getParentIndex()).getWeatherEntityList().get(0).getLatitude() == 0.0) {
            searchWeather(weatherDataList.get(eventBusAddNewData.getParentIndex()).getCityName());
        } else {
            double latitude = PrefUtils.getDoubleFromPref(context, Constants.PrefConstants.LATITUDE, 0.0);
            double longitude = PrefUtils.getDoubleFromPref(context, Constants.PrefConstants.LONGITUDE, 0.0);
            getWeatherData(latitude, longitude);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusRefreshData(EventBusRefreshWeatherList eventBusRefreshWeatherList) {
        new GetWeatherDataList(context).execute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchCity(EventBusSearchCity eventBusSearchCity) {
        searchWeather(eventBusSearchCity.getCityName());
    }

    //    TODO: Retrofit methods....
    private void getWeatherData(double latitude, double longitude) {
        currentWeatherCall = ApiClient.getClient().create(WeatherApis.class)
                .getCurrentWeather(Constants.RetrofitConstants.WEATHER_MAP_API_KEY, String.valueOf(latitude), String.valueOf(longitude));
        currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NotNull Call<CurrentWeather> call, @NotNull Response<CurrentWeather> response) {
                if (response != null && response.isSuccessful()) {
                    CurrentWeather currentWeather = response.body();
                    if (currentWeather != null) {
                        addWeatherData(currentWeather);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<CurrentWeather> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void searchWeather(String cityName) {
        currentWeatherCall = ApiClient.getClient().create(WeatherApis.class)
                .searchByName(Constants.RetrofitConstants.WEATHER_MAP_API_KEY, cityName);
        currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NotNull Call<CurrentWeather> call, @NotNull Response<CurrentWeather> response) {
                if (response != null && response.isSuccessful()) {
                    CurrentWeather currentWeather = response.body();
                    if (currentWeather != null) {
                        addWeatherData(currentWeather);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<CurrentWeather> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addWeatherData(CurrentWeather currentWeather) {
        @SuppressLint("SimpleDateFormat") String timestamp = new SimpleDateFormat(Constants.TIME_FORMAT).format(new Date());
        int iconId = R.drawable.ic_action_dunno;
        String condition = "";
        if (currentWeather.getWeather() != null && currentWeather.getWeather().size() > 0) {
            iconId = currentWeather.getWeather().get(0).getId();
            condition = currentWeather.getWeather().get(0).getMain();
        }

        WeatherEntity weatherEntity = new WeatherEntity(timestamp,
                0.0, 0.0, currentWeather.getName(), currentWeather.getMain().getTemp(),
                currentWeather.getSys().getCountry(), iconId, condition, currentWeather.getWind().getSpeed(),
                currentWeather.getMain().getPressure(), currentWeather.getMain().getTempMax(), currentWeather.getMain().getTempMin());
        new InsertWeatherTask(context).execute(weatherEntity);
    }

    //    Room CRUD methods....
    class InsertWeatherTask extends AsyncTask<WeatherEntity, Void, Void> {

        Context context;

        InsertWeatherTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(WeatherEntity... weatherEntities) {
            DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao()
                    .insertNewWeather(weatherEntities[0]);
            for (int i = 0; i < weatherDataList.size(); i++) {
                if (weatherDataList.get(i).getCityName().equals(weatherEntities[0].getLocationName())) {
                    weatherDataList.get(i).getWeatherEntityList().add(weatherEntities[0]);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetWeatherDataList(context).execute();
        }
    }

    class GetWeatherDataList extends AsyncTask<Void, Void, List<WeatherEntity>> {

        Context context;

        GetWeatherDataList(Context context) {
            this.context = context;
        }

        @Override
        protected List<WeatherEntity> doInBackground(Void... voids) {
            return DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao()
                    .getWeatherList();
        }

        @Override
        protected void onPostExecute(List<WeatherEntity> aVoid) {
            super.onPostExecute(aVoid);

            weatherDataList.clear();
            HashMap<String, List<WeatherEntity>> hashMap = new HashMap<>();

            for (WeatherEntity weatherEntity : aVoid) {
                if (hashMap.get(weatherEntity.getLocationName()) == null) {
                    hashMap.put(weatherEntity.getLocationName(), new ArrayList<WeatherEntity>());
                    List<WeatherEntity> weatherEntityList = hashMap.get(weatherEntity.getLocationName());
                    if (weatherEntityList != null) {
                        weatherEntityList.add(weatherEntity);
                        hashMap.put(weatherEntity.getLocationName(), weatherEntityList);
                    }
                } else {
                    List<WeatherEntity> weatherEntityList = hashMap.get(weatherEntity.getLocationName());
                    if (weatherEntityList != null) {
                        weatherEntityList.add(weatherEntity);
                        hashMap.put(weatherEntity.getLocationName(), weatherEntityList);
                    }
                }
            }
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                ArrayList<WeatherEntity> weatherEntityList = (ArrayList<WeatherEntity>) pair.getValue();
                Collections.sort(weatherEntityList, (t1, t2) -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
                    Date date1 = null, date2 = null;
                    try {
                        date1 = simpleDateFormat.parse(t1.getTimestamp());
                        date2 = simpleDateFormat.parse(t2.getTimestamp());
                    } catch (Exception e) {

                    }
                    return Long.compare(date2.getTime(), date1.getTime());
                });
                weatherDataList.add(new WeatherData((String) pair.getKey(), weatherEntityList));
                iterator.remove();
            }

            weatherDataMainListPagerAdapter = new WeatherDataMainListPagerAdapter(context, weatherDataList);
            viewPagerMain.setAdapter(weatherDataMainListPagerAdapter);
            viewPagerMain.setOffscreenPageLimit(weatherDataList.size() - 1);

        }

    }

    class DeleteWeatherDataTask extends AsyncTask<WeatherEntity, Void, Void> {

        Context context;
        int position;

        DeleteWeatherDataTask(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        @Override
        protected Void doInBackground(WeatherEntity... weatherEntities) {
            DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao()
                    .deleteWeatherData(weatherEntities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetWeatherDataList(context).execute();
        }
    }
}