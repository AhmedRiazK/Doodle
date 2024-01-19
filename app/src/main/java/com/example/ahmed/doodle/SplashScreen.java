package com.example.ahmed.doodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

import com.example.doodle.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int splashtime = 3000;
        super.onCreate(savedInstanceState);
        Intent intent;
        intent = getIntent();
        View v = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN;
        v.setSystemUiVisibility(ui);
        setContentView(R.layout.activity_splash_screen);
        boolean handler = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,SelectionActivity.class);
                startActivity(i);
                finish();
            }
        }, splashtime);
    }
}
