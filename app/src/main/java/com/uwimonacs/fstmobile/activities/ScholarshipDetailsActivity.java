package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class ScholarshipDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets data for the relevant card details
        final Bundle extras = getIntent().getExtras();
        final String scholName = extras.getString("scholName");
        final String scholDetails = extras.getString("scholDetails");

        setContentView(R.layout.activity_scholarship_details);

        final TextView scholNameTextView = (TextView) findViewById(R.id.name);
        final TextView scholDetailsTextView = (TextView) findViewById(R.id.detail);

        scholNameTextView.setText(scholName);
        scholDetailsTextView.setText(Html.fromHtml(scholDetails).toString().trim());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return true;
        }
    }
}
