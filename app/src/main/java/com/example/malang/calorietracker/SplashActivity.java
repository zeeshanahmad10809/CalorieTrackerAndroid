package com.example.malang.calorietracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Thread(){
            public void run(){
                try{
                    sleep(2*1000);
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));


                }
                catch (Exception exception){

                }

            }
        }.start();


        
    }
}
