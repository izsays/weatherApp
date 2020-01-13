package com.vodworks.myweatherapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vodworks.myweatherapp.R;

public class SplashActivity extends AppCompatActivity {

    //    Android fields....
    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        handler = new Handler();

        handler.postDelayed(() -> {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_close_enter);
            finish();
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}