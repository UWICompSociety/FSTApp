package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.util.GsonExclude;
import com.uwimonacs.fstmobile.models.Contact;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/5/2016.
 */
public class RestContact {
    private String url = "";

    public RestContact(String url) {
        this.url = url; //url to connect to rest api eg.localhost/api/contacts
    }

    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts;

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
            contacts = gson.fromJson(reader, new TypeToken<List<Contact>>(){}.getType());

        } catch(MalformedURLException mal) {
            return null;
        } catch(IOException io) {
            return null;
        }

        return contacts;
    }
}
