package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.helper.GsonExclude;
import com.uwimonacs.fstmobile.models.News;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/11/2016.
 */
public class RestNews {
    private String url;

    public RestNews(String url)
    {
        this.url = url;
    }

    public ArrayList<News> getNews() {
        ArrayList<News> newsItems;
        try {
            final HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // gets the data from the rest api
            final Reader reader = new InputStreamReader(conn.getInputStream());
            final GsonExclude exclude = new GsonExclude(); // used to speed serialization process

            final Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            newsItems = gson.fromJson(reader, new TypeToken<List<News>>(){}.getType());
        } catch(MalformedURLException mal) {
            return null;
        } catch(IOException i) {
            return null;
        }

        return newsItems;
    }
}
