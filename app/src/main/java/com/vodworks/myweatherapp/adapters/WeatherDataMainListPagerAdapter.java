package com.vodworks.myweatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.model.WeatherData;
import com.vodworks.myweatherapp.model.eventbus.EventBusAddNewData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class WeatherDataMainListPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<WeatherData> weatherDataList;

    public WeatherDataMainListPagerAdapter(Context context, List<WeatherData> weatherDataList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.weatherDataList = weatherDataList;
    }

    @Override
    public int getCount() {
        return weatherDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_item_weather_list, container, false);

        TextView textViewCityName = view.findViewById(R.id.textViewTitlePagerItemWeatherList);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPagerItemWeatherList);
        AppCompatImageView imageViewRefresh = view.findViewById(R.id.imageViewRefresh);

        if (weatherDataList.get(position).getWeatherEntityList().get(0).getLatitude() != 0.0) {
            textViewCityName.setText("Your Location");
        } else {
            textViewCityName.setText(weatherDataList.get(position).getCityName());
        }
        WeatherDataListAdapter weatherDataListAdapter = new WeatherDataListAdapter(context, weatherDataList.get(position).getWeatherEntityList());
        weatherDataListAdapter.setParentIndex(position);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(weatherDataListAdapter);

        imageViewRefresh.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventBusAddNewData(position));
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
