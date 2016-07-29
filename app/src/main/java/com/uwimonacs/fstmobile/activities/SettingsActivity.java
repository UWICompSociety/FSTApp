package com.uwimonacs.fstmobile.activities;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.uwimonacs.fstmobile.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}