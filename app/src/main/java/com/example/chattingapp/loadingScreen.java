package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class loadingScreen extends AppCompatActivity {

    // determines the duration of this loading screen, passed to the delay handler
    private static int timer = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);


        // getting title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // handler to delay this activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // after this loading screen is delayed i want to go to the main activity
                Intent intent = new Intent(loadingScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },timer);
    }
}