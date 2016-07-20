package com.uwimonacs.fstmobile;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.helper.Constants;

/**
 * Created by Matthew on 7/6/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
