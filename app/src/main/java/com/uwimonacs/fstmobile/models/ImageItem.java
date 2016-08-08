package com.uwimonacs.fstmobile.models;

import android.graphics.Bitmap;

/**
 * Created by Matthew on 8/8/2016.
 */
public class ImageItem {

    String url;
    private String title;


    public ImageItem(String url,String title)
    {
        this.url = url;
        this.title =title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
