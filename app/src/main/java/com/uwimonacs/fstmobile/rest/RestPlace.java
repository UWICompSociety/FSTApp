package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.util.GsonExclude;
import com.uwimonacs.fstmobile.models.Place;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/19/2016.
 */
public class RestPlace {
    private String url;

    public RestPlace(String url)
    {
        this.url = url;
    }

    public ArrayList<Place> getPlaces() {
        ArrayList<Place> places;
        try {
            final HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // gets the data from the rest api
            final Reader reader = new InputStreamReader(conn.getInputStream());
            final GsonExclude exclude = new GsonExclude(); //used to speed serialization process

            final Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            // gets a list of places from the api
            places = gson.fromJson(reader, new TypeToken<List<Place>>(){}.getType());

        } catch(MalformedURLException mal) {
            return null;
        } catch(IOException i) {
            return null;
        }

        return places;
    }
}
