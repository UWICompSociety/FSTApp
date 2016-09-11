package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.messaging.FirebaseMessaging;
import com.uwimonacs.fstmobile.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        setContentView(R.layout.activity_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.preferences);

            setUpPreferenceListeners();
        }

        public void setUpPreferenceListeners(){
            final Preference pref_news = findPreference("pref_news");
            pref_news.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(o == Boolean.TRUE) {
                        FirebaseMessaging.getInstance().subscribeToTopic("news");
                        System.out.println("Subscribing to news");
                    }
                    else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                        System.out.println("Unsubscribed from news");
                    }
                    return true;
                }
            });

            final Preference pref_events = findPreference("pref_events");
            pref_events.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(o == Boolean.TRUE)
                        FirebaseMessaging.getInstance().subscribeToTopic("events");
                    else
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("events");
                    return true;
                }
            });

            final Preference pref_gallery = findPreference("pref_gallery");
            pref_gallery.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(o == Boolean.TRUE)
                        FirebaseMessaging.getInstance().subscribeToTopic("gallery");
                    else
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("gallery");
                    return true;
                }
            });

            final Preference pref_schol = findPreference("pref_schol");
            pref_schol.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(o == Boolean.TRUE)
                        FirebaseMessaging.getInstance().subscribeToTopic("schol");
                    else
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("schol");
                    return true;
                }
            });
        }
    }
}
