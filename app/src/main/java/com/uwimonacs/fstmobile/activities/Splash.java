package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

        try {
            handleNotification();
        } catch (ClassNotFoundException exception){
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    private void handleNotification() throws ClassNotFoundException{
        if(getIntent().hasExtra("activity")) {
            Intent intent = new Intent();
            Class launchClass;
            String activity = (String) getIntent().getExtras().get("activity");
            switch (activity) {
                case "News":
                    launchClass = Class.forName("com.uwimonacs.fstmobile.activities.MainActivity");
                    break;
                case "Events":
                    launchClass = Class.forName("com.uwimonacs.fstmobile.activities.EventActivity");
                    break;
                case "Gallery":
                    launchClass = Class.forName("com.uwimonacs.fstmobile.activities.GalleryActivity");
                    break;
                case "Scholarships":
                    launchClass = Class.forName("com.uwimonacs.fstmobile.activities.ScholarshipActivity");
                    break;
                default:
                    launchClass = null;
                    break;
            }

            if(launchClass != null){
                intent.setClass(this, launchClass);
                startActivity(intent);
                finish();
            }
        } else {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }
}
