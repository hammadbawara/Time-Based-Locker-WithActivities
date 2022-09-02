package com.hz_apps.timebasedlocker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.hz_apps.timebasedlocker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MaterialToolbar toolbar;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applySettings();
        toolbar = binding.toolbarMainActivity;
        optionMenu();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_videos, R.id.navigation_photos, R.id.navigation_others)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.bottomNavMainActivity, navController);

        NavigationUI.setupWithNavController(binding.toolbarMainActivity, navController, appBarConfiguration);
    }

    private void optionMenu(){
        toolbar.inflateMenu(R.menu.option_menu_main_acitivity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Settings")){
                    navController.navigate(R.id.settingsFragment);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void applySettings(){
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