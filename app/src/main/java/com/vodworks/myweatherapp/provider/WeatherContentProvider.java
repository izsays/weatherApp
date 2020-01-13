package com.vodworks.myweatherapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vodworks.myweatherapp.database.DatabaseClient;
import com.vodworks.myweatherapp.database.dao.WeatherDao;
import com.vodworks.myweatherapp.database.entities.WeatherEntity;

public class WeatherContentProvider extends ContentProvider {

    /**
     * The authority of this content provider.
     */
    public static final String AUTHORITY = "com.vodworks.weatherapp.provider";

    /**
     * The URI for the WeatherEntity table.
     */
    public static final Uri URI_CHEESE = Uri.parse(
            "content://" + AUTHORITY + "/" + "WeatherEntity");

    /**
     * The match code for some items in the WeatherEntity table.
     */
    private static final int CODE_WEATHER_DIR = 1;

    /**
     * The match code for an item in the WeatherEntity table.
     */
    private static final int CODE_WEATHER_ITEM = 2;

    /**
     * The URI matcher.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "WeatherEntity", CODE_WEATHER_DIR);
        MATCHER.addURI(AUTHORITY, "WeatherEntity" + "/*", CODE_WEATHER_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int code = MATCHER.match(uri);
        if (code == CODE_WEATHER_DIR || code == CODE_WEATHER_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            WeatherDao weatherDao = DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao();
            final Cursor cursor;
            if (code == CODE_WEATHER_DIR) {
                cursor = weatherDao.selectAll();
            } else {
                cursor = weatherDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_WEATHER_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "WeatherEntity";
            case CODE_WEATHER_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "WeatherEntity";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)) {
            case CODE_WEATHER_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = DatabaseClient.getDatabaseClient(context).getWeatherDatabase().getWeatherDao()
                        .insert(WeatherEntity.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_WEATHER_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
