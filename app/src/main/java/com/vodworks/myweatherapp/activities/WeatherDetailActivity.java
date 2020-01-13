package com.vodworks.myweatherapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.common.Constants;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;
import com.vodworks.myweatherapp.fragments.WeatherDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.appBarWeatherDetail)
    Toolbar toolbarDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarDetail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            ArrayList<WeatherEntity> weatherEntityList = getIntent().getParcelableArrayListExtra(Constants.BundleConstants.WEATHER_DATA_LIST);
            int clickedIndex = getIntent().getIntExtra(Constants.BundleConstants.CLICKED_INDEX, 0);

            if (weatherEntityList != null) {
                WeatherDetailFragment weatherDetailFragment = WeatherDetailFragment.newInstance(weatherEntityList, clickedIndex);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayoutWeatherDetailContainer, weatherDetailFragment)
                        .commit();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
    }

}