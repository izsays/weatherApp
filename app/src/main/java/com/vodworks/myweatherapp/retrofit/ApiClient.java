package com.vodworks.myweatherapp.retrofit;

import com.vodworks.myweatherapp.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    private ApiClient() {

    }

    public static Retrofit getClient() {

        if (retrofit == null) {
            String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    response.header("Content-Type", APPLICATION_JSON_CHARSET_UTF_8);
                    response.header("Accept", APPLICATION_JSON_CHARSET_UTF_8);
                    return response;
                }
            }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            OkHttpClient client = httpClient.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.RetrofitConstants.WEATHER_MAP_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}