package com.hz_apps.timebasedlocker.ui.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.hz_apps.timebasedlocker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);



    }
}