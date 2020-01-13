package com.vodworks.myweatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.List;

import static com.vodworks.myweatherapp.utils.WeatherIconUtils.getWeatherIconAnimation;

public class WeatherDataListPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private List<WeatherEntity> weatherEntityList;

    public WeatherDataListPagerAdapter(Context context, List<WeatherEntity> weatherEntityList) {
        this.weatherEntityList = weatherEntityList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return weatherEntityList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_item_weather_data, container, false);
        WeatherEntity weatherEntity = weatherEntityList.get(position);

        TextView textViewLocation = view.findViewById(R.id.textViewLocation);
        TextView textViewTemperature = view.findViewById(R.id.textViewTemperature);
        TextView textViewCondition = view.findViewById(R.id.textViewCondition);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        TextView textViewAirSpeed = view.findViewById(R.id.textViewAirSpeed);
        TextView textViewPressure = view.findViewById(R.id.textViewPressure);
        TextView textViewMaxTemp = view.findViewById(R.id.textViewMaxTemp);
        TextView textViewMinTemp = view.findViewById(R.id.textViewMinTemp);

        lottieAnimationView.setAnimation(getWeatherIconAnimation(weatherEntity.getWeatherIconId()));
        lottieAnimationView.playAnimation();

        textViewLocation.setText(weatherEntity.getLocationName() + ", " + weatherEntity.getCountry());
        textViewTemperature.setText(getTempFormat(weatherEntity.getTemperature()));
        textViewDate.setText(weatherEntity.getTimestamp());
        textViewCondition.setText(weatherEntity.getCondition());
        textViewAirSpeed.setText(weatherEntity.getAirSpeed() + "km/h");
        textViewPressure.setText(String.valueOf(weatherEntity.getPressure()));
        textViewMaxTemp.setText(getTempFormat(weatherEntity.getMaxTemp()));
        textViewMinTemp.setText(getTempFormat(weatherEntity.getMinTemp()));

        container.addView(view);
        return view;
    }

    private String getTempFormat(double temperature) {
        return String.format("%.1f", temperature - 273.5) + "°C";
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
