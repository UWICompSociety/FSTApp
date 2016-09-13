package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.uwimonacs.fstmobile.R;

@SuppressWarnings("ConstantConditions")
public class QuickAlertsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_alerts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_quick_alerts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
