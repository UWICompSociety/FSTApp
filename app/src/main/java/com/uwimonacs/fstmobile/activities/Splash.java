package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uwimonacs.fstmobile.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        * No need to set content view. The background is set in the style as a drawable so it is
        * available immediately at launch and is shown until the app finishes initializing and
        * runs the code below
        * */

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
