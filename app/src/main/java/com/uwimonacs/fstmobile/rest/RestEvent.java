package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.util.GsonExclude;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 8/8/2016.
 */
public class RestEvent {

    private String url = "";

    public RestEvent(String url) {
        this.url = url; //url to connect to rest api eg.localhost/api/events
    }

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events;

        try {
            final HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();

            conn.setRequestMethod("GET");

            // gets the data from the rest api
            final Reader reader = new InputStreamReader(conn.getInputStream());

            // used to speed serialization process
            final GsonExclude exclude = new GsonExclude();

            final Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            // gets a list of contacts from the api
            events = gson.fromJson(reader, new TypeToken<List<Event>>(){}.getType());

        } catch(MalformedURLException mal) {
            return null;
        } catch(IOException io) {
            return null;
        }

        return events;
    }
}
