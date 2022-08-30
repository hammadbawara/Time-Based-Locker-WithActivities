package com.hz_apps.timebasedlocker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.databinding.ActivitySplashBinding;

import java.util.concurrent.Executors;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create database instance for all over application
        Executors.newSingleThreadExecutor().execute(this::tasks);


    }

    private void tasks(){
        DBHelper.createInstanceForOverApplication(getApplication());
        finishActivity();
    }

    private void finishActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


}