package com.uwimonacs.fstmobile;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.support.multidex.MultiDex;
import android.webkit.WebView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.SASConfig;

/**
 * Created by Matthew on 7/6/2016.
 */
public class MyApplication extends com.activeandroid.app.Application {
    private static WebView webView;
    private static SASConfig sasConfig;
    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        context = getApplicationContext();
        resources = getResources();
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            sasConfig = (SASConfig) new Select().from(SASConfig.class).where("ConfigID = ?", 0).execute().get(0);
        } catch (Exception e){}
        if(sasConfig != null) {
            sasConfig.initialize(resources, context);
            sasConfig.unSerialize();
        }
        else {
            sasConfig = new SASConfig();
            sasConfig.initialize(resources, context);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
