package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.mapActivity_MVP.MapActivity;

/**
 * Created by Akinyele on 8/18/2017.
 */

public class MapSplashscreen extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_splashscreen);

        final Intent mapIntent = new Intent(this, MapActivity.class);
        Thread thread = new Thread(){
            public void run(){
                try{
                     sleep(2000);
                }catch(InterruptedException e){}
                finally {
                    startActivity(mapIntent);
                    finish();
                }
            }
        };
        thread.start();
    }



}
