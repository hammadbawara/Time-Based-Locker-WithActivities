package com.hz_apps.timebasedlocker.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hz_apps.timebasedlocker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);



    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav_main_activity);
        if (bottomNav != null) bottomNav.setVisibility(View.INVISIBLE);
        listPreference = (ListPreference) getPreferenceManager().findPreference("application_theme");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                changeTheme(newValue);
                return true;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void changeTheme(Object newValue){
        int theme = -1;
        String[] theme_values = getResources().getStringArray(R.array.themes_values);
        if (newValue.equals(theme_values[1]))theme = AppCompatDelegate.MODE_NIGHT_YES;
        else if (newValue.equals(theme_values[2]))  theme = AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(theme);   //Set theme
    }
}