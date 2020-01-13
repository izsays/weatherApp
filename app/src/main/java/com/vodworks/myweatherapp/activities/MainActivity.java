package com.vodworks.myweatherapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.vodworks.myweatherapp.R;
import com.vodworks.myweatherapp.backgroundwork.LocationService;
import com.vodworks.myweatherapp.common.Constants;
import com.vodworks.myweatherapp.fragments.WeatherDetailFragment;
import com.vodworks.myweatherapp.model.eventbus.EventBusNewLocation;
import com.vodworks.myweatherapp.model.eventbus.EventBusSearchCity;
import com.vodworks.myweatherapp.model.eventbus.EventBusWeatherListClick;
import com.vodworks.myweatherapp.utils.RuntimePermissionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //    Constants....
    private static final int LOCATION_PERMISSION_CODE = 1000;

    //    Android fields....
    @BindView(R.id.appBarMain)
    Toolbar toolbarMain;
    private FrameLayout frameLayoutContainer;
    private Context context;
    private SearchView searchView;
    private FragmentManager fragmentManager;

    //    Service fields....
    private Intent locationServiceIntent;

    //    Instance fields....
    private boolean isInTab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        context = this;

        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        fragmentManager = getSupportFragmentManager();

        //        Checking runtime location permissions....
        RuntimePermissionUtils.checkLocationPermission(context, LOCATION_PERMISSION_CODE,
                isAllowed -> {
                    if (isAllowed) {
                        startLocationService();
                    }
                });

//        Checking if device is tablet or phone....
        frameLayoutContainer = findViewById(R.id.frameLayoutWeatherDetailContainer);
        if (frameLayoutContainer != null) {
            isInTab = true;
        }

    }

    private void startLocationService() {
        locationServiceIntent = new Intent(context, LocationService.class);
        startService(locationServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (locationServiceIntent != null) {
            stopService(locationServiceIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menuSearch)
                .getActionView();
        searchView.setQueryHint("Search location....");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    EventBus.getDefault().post(new EventBusSearchCity(query));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusWeatherDataClick(EventBusWeatherListClick eventBusWeatherListClick) {
        if (isInTab) {
            WeatherDetailFragment weatherDetailFragment = WeatherDetailFragment.newInstance(eventBusWeatherListClick.getWeatherEntityList(), eventBusWeatherListClick.getParentIndex());
            fragmentManager.beginTransaction().replace(R.id.frameLayoutWeatherDetailContainer, weatherDetailFragment).commit();
        } else {
            Intent intent = new Intent(context, WeatherDetailActivity.class);
            intent.putParcelableArrayListExtra(Constants.BundleConstants.WEATHER_DATA_LIST, eventBusWeatherListClick.getWeatherEntityList());
            intent.putExtra(Constants.BundleConstants.CLICKED_INDEX, eventBusWeatherListClick.getPosition());
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNewLocation(EventBusNewLocation eventBusRefreshLocation) {
        if (locationServiceIntent != null) {
            stopService(locationServiceIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationService();
                else
                    Toast.makeText(context, "Could not get your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}