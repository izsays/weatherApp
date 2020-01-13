package com.vodworks.myweatherapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RuntimePermissionUtils {

    public interface RuntimePermissionResult {
        void onResult(boolean isAllowed);
    }

    public static void checkLocationPermission(Context context, int requestCode, RuntimePermissionResult runtimePermissionResult) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
                runtimePermissionResult.onResult(false);
            } else {
                runtimePermissionResult.onResult(true);
            }
        } else {
            runtimePermissionResult.onResult(true);
        }
    }


}
