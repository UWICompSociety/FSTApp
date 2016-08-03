package com.uwimonacs.fstmobile;

import android.app.Application;
import android.content.Context;
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
        webView = new WebView(getApplicationContext());
        webView.getSettings().setJavaScriptEnabled(true);
        sasConfig = new SASConfig();
        sasConfig.initialize(getResources(), getApplicationContext());
        webView.addJavascriptInterface(sasConfig, "sasConfig");
        context = getApplicationContext();
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
