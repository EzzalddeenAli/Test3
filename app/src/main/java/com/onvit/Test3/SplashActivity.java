package com.onvit.Test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // delay
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //fade in, fade out
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            finish();
        }, 3000);
    }
}
