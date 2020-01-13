package com.vodworks.myweatherapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.adapters.WeatherDataListPagerAdapter;
import com.vodworks.myweatherapp.common.Constants;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherDetailFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.viewPagerDetail)
    ViewPager viewPagerDetail;
    private Context context;

    private WeatherDetailFragment() {

    }

    public static WeatherDetailFragment newInstance(ArrayList<WeatherEntity> weatherEntityList,
                                                    int clickedIndex) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.BundleConstants.WEATHER_DATA_LIST, weatherEntityList);
        bundle.putInt(Constants.BundleConstants.CLICKED_INDEX, clickedIndex);
        WeatherDetailFragment weatherDetailFragment = new WeatherDetailFragment();
        weatherDetailFragment.setArguments(bundle);
        return weatherDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        ButterKnife.bind(this, view);
        context = getContext();

        if (getArguments() != null) {

            ArrayList<WeatherEntity> weatherEntityList = getArguments().getParcelableArrayList(Constants.BundleConstants.WEATHER_DATA_LIST);
            int clickedIndex = getArguments().getInt(Constants.BundleConstants.CLICKED_INDEX);

            if (weatherEntityList != null) {
                WeatherDataListPagerAdapter weatherDataListPagerAdapter = new WeatherDataListPagerAdapter(context, weatherEntityList);
                viewPagerDetail.setAdapter(weatherDataListPagerAdapter);
                viewPagerDetail.setCurrentItem(clickedIndex);
            }

        }

        return view;
    }

}