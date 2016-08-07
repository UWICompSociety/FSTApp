package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.News;
import com.uwimonacs.fstmobile.rest.RestNews;

import java.util.ArrayList;

/**
 * Created by Matthew on 7/11/2016.
 */
public class NewsSync {
    private final String url;
    private ArrayList<News> newsItems;

    public NewsSync(String url)
    {
        this.url = url;
    }


    public boolean syncNews() {
        final RestNews restNews = new RestNews(url);

        newsItems = restNews.getNews(); // gets the list of news from rest api

        if (newsItems == null) // if there are no news
            return false;

        if (newsItems.size() == 0) // if the news list is empty
            return false;

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < newsItems.size(); i++) {
                News news = newsItems.get(i);

                News.findOrCreateFromJson(news); // saves contact to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }
}
