package com.vodworks.myweatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.ref.WeakReference;

public class PrefUtils {

    public static void saveToPrefs(Context context, String key, double value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(key, Double.doubleToRawLongBits((double) value)).apply();
    }

    public static double getDoubleFromPref(Context context, String key, double defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public static void removeFromPrefs(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();

    }

    public static boolean hasKey(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            return prefs.contains(key);
        }
        return false;
    }

}