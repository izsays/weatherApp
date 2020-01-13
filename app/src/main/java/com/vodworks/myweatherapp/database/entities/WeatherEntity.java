package com.vodworks.myweatherapp.database.entities;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class WeatherEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "timestamp")
    private String timestamp;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "location_name")
    private String locationName;
    @ColumnInfo(name = "temperature")
    private double temperature;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "weather_icon_id")
    private int weatherIconId;
    @ColumnInfo(name = "condition")
    private String condition;
    @ColumnInfo(name = "air_speed")
    private double airSpeed;
    @ColumnInfo(name = "pressure")
    private int pressure;
    @ColumnInfo(name = "max_temp")
    private double maxTemp;
    @ColumnInfo(name = "min_temp")
    private double minTemp;

    @Ignore
    public WeatherEntity() {

    }

    public WeatherEntity(String timestamp, double latitude, double longitude,
                         String locationName, double temperature, String country,
                         int weatherIconId, String condition, double airSpeed, int pressure,
                         double maxTemp, double minTemp) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.temperature = temperature;
        this.country = country;
        this.weatherIconId = weatherIconId;
        this.condition = condition;
        this.airSpeed = airSpeed;
        this.pressure = pressure;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    protected WeatherEntity(Parcel in) {
        id = in.readInt();
        timestamp = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        locationName = in.readString();
        temperature = in.readDouble();
        country = in.readString();
        weatherIconId = in.readInt();
        condition = in.readString();
        airSpeed = in.readDouble();
        pressure = in.readInt();
        maxTemp = in.readDouble();
        minTemp = in.readDouble();
    }

    public static final Creator<WeatherEntity> CREATOR = new Creator<WeatherEntity>() {
        @Override
        public WeatherEntity createFromParcel(Parcel in) {
            return new WeatherEntity(in);
        }

        @Override
        public WeatherEntity[] newArray(int size) {
            return new WeatherEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(int weatherIconId) {
        this.weatherIconId = weatherIconId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getAirSpeed() {
        return airSpeed;
    }

    public void setAirSpeed(double airSpeed) {
        this.airSpeed = airSpeed;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(timestamp);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(locationName);
        parcel.writeDouble(temperature);
        parcel.writeString(country);
        parcel.writeInt(weatherIconId);
        parcel.writeString(condition);
        parcel.writeDouble(airSpeed);
        parcel.writeInt(pressure);
        parcel.writeDouble(maxTemp);
        parcel.writeDouble(minTemp);
    }

    public static WeatherEntity fromContentValues(ContentValues values) {
        int id = values.getAsInteger("id");
        String timestamp = values.getAsString("timestamp");
        double latitude = values.getAsDouble("latitude");
        double longitude = values.getAsDouble("longitude");
        String locationName = values.getAsString("location_name");
        double temperature = values.getAsDouble("temperature");
        String country = values.getAsString("country");
        int weatherIconId = values.getAsInteger("weather_icon_id");
        String condition = values.getAsString("condition");
        double airSpeed = values.getAsDouble("air_speed");
        int pressure = values.getAsInteger("pressure");
        double maxTemp = values.getAsDouble("max_temp");
        double minTemp = values.getAsDouble("min_temp");

        final WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.id = id;
        weatherEntity.timestamp = timestamp;
        weatherEntity.latitude = latitude;
        weatherEntity.longitude = longitude;
        weatherEntity.locationName = locationName;
        weatherEntity.temperature = temperature;
        weatherEntity.country = country;
        weatherEntity.weatherIconId = weatherIconId;
        weatherEntity.condition = condition;
        weatherEntity.airSpeed = airSpeed;
        weatherEntity.pressure = pressure;
        weatherEntity.maxTemp = maxTemp;
        weatherEntity.minTemp = minTemp;

        return weatherEntity;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", locationName='" + locationName + '\'' +
                ", temperature=" + temperature +
                ", country='" + country + '\'' +
                ", weatherIconId=" + weatherIconId +
                ", condition='" + condition + '\'' +
                ", airSpeed=" + airSpeed +
                ", pressure=" + pressure +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                '}';
    }
}
