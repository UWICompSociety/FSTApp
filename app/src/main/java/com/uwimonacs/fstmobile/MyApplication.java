package com.uwimonacs.fstmobile;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.webkit.WebView;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.SASConfig;

/**
 * Created by Matthew on 7/6/2016.
 */
public class MyApplication extends Application {
    private static WebView webView;
    private static SASConfig sasConfig;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        context = getApplicationContext();
        webView = new WebView(getApplicationContext());
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            sasConfig = SASConfig.load(SASConfig.class, 0);
        } catch (SQLiteException e){}
        if(sasConfig != null) {
            sasConfig.initialize(getResources(), context);
            sasConfig.unSerialize();
        }
        else {
            sasConfig = new SASConfig();
            sasConfig.initialize(getResources(), context);
        }
        webView.addJavascriptInterface(sasConfig, "sasConfig");
    }

    public static WebView getWebView(){
        return webView;
    }

    public static SASConfig getSasConfig() {
        return sasConfig;
    }

    public static Context getContext(){
        return context;
    }
}
