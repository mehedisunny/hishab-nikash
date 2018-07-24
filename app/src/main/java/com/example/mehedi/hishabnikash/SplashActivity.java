package com.example.mehedi.hishabnikash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.mehedi.hishabnikash.pin_code.PinCodeVerificationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);

        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
                String pinCode = sharedPreferences.getString("pin", "hello");

                if (pinCode.equals("hello")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, PinCodeVerificationActivity.class));
                    finish();
                }
            }
        }, secondsDelayed * 1000);
    }

}
