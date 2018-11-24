package com.dwipa.user.csc.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashscreenActivity extends AppCompatActivity {
    ImageView tvSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.dwipa.user.csc.R.layout.activity_splashscreen);

        tvSplash = (ImageView) findViewById(com.dwipa.user.csc.R.id.tvSplash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }, 3000L); //3000L = 3 detik
    }
}
