package com.vodworks.myweatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;
import com.vodworks.myweatherapp.model.eventbus.EventBusLongClick;
import com.vodworks.myweatherapp.model.eventbus.EventBusWeatherListClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vodworks.myweatherapp.utils.WeatherIconUtils.getWeatherIconAnimation;

public class WeatherDataListAdapter extends RecyclerView.Adapter<WeatherDataListAdapter.WeatherDataListHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<WeatherEntity> weatherEntityList;
    private int parentIndex;

    public WeatherDataListAdapter(Context context, ArrayList<WeatherEntity> weatherEntityList) {
        this.context = context;
        this.weatherEntityList = weatherEntityList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setParentIndex(int index) {
        this.parentIndex = index;
    }

    @NonNull
    @Override
    public WeatherDataListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherDataListHolder(layoutInflater.inflate(R.layout.row_weather_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataListHolder holder, int position) {
        WeatherEntity weatherEntity = weatherEntityList.get(position);
        holder.textViewTemperature.setText(String.format("%.1f", weatherEntity.getTemperature() - 273.5) + "°C");
        holder.textViewDate.setText(weatherEntity.getTimestamp());
        holder.textViewLocation.setText(weatherEntity.getLocationName());
        holder.lottieAnimationViewWeather.setAnimation(getWeatherIconAnimation(weatherEntity.getWeatherIconId()));
    }

    @Override
    public int getItemCount() {
        return weatherEntityList.size();
    }

    class WeatherDataListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTemperatureWeatherDataRow)
        TextView textViewTemperature;
        @BindView(R.id.textViewDateWeatherDataRow)
        TextView textViewDate;
        @BindView(R.id.textViewLocationWeatherDataRow)
        TextView textViewLocation;
        @BindView(R.id.lottieWeatherTypeWeatherDataRow)
        LottieAnimationView lottieAnimationViewWeather;

        public WeatherDataListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnLongClickListener(view -> {
                EventBus.getDefault().post(new EventBusLongClick(getAdapterPosition(), itemView, parentIndex));
                return true;
            });

            itemView.setOnClickListener(view -> {
                EventBus.getDefault().post(new EventBusWeatherListClick(getAdapterPosition(), parentIndex, weatherEntityList));
            });
        }
    }
}
