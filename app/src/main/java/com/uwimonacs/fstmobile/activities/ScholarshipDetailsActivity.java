package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class ScholarshipDetailsActivity extends AppCompatActivity {

    private Bundle extras;
    private String scholName;
    private String scholDetails;
    private ImageView  scholPic;
    private TextView scholNameTextView;
    private TextView scholDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Gets data for the relevant card details
        extras = getIntent().getExtras();
        scholName = extras.getString("scholName");
        scholDetails = extras.getString("scholDetails");

        setContentView(R.layout.activity_scholarship_details);

        scholPic = (ImageView) findViewById(R.id.photo);
        scholNameTextView = (TextView) findViewById(R.id.name);
        scholDetailsTextView = (TextView) findViewById(R.id.detail);

        //Sets details of scholarship to be displayed
        scholPic.setImageResource(R.drawable.scholarship);
        scholNameTextView.setText(scholName);
        scholDetailsTextView.setText(scholDetails);
    }
}
