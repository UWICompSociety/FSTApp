package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.uwimonacs.fstmobile.R;

// If you use the Google Maps Android API in your application,
// you must include the Google Play Services attribution text
// as part of a "Legal Notices" section in your application.
public class LegalNoticesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_legal_notices);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TextView legalInfoTextView = (TextView) findViewById(R.id.legal_info);

        String openSourceSoftwareLicenseInfo =
                GoogleApiAvailability.getInstance().getOpenSourceSoftwareLicenseInfo(this);
        if(openSourceSoftwareLicenseInfo != null)
            legalInfoTextView.setText(openSourceSoftwareLicenseInfo);
        else
            legalInfoTextView.setText(R.string.play_services_not_installed);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
