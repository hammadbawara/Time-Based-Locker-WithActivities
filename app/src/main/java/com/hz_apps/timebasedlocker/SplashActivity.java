package com.hz_apps.timebasedlocker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

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
        setTheme();
        DBHelper.createInstanceForOverApplication(getApplication());
        finishActivity();
    }

    private void finishActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void setTheme(){
        SharedPreferences settingsPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Theme settings
        String saved_theme_value = settingsPreferences.getString("application_theme", "default");
        String[] theme_values = getResources().getStringArray(R.array.themes_values);
        int theme = -1;
        if (saved_theme_value.equals(theme_values[1]))theme = AppCompatDelegate.MODE_NIGHT_YES;
        else if (saved_theme_value.equals(theme_values[2]))  theme = AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(theme);   //Set theme
    }


}