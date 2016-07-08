package com.uwimonacs.fstmobile.rest;

import android.os.Debug;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.helper.GsonExclude;
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

    String url = "";

    public RestContact(String url)
    {
        this.url = url; //url to connect to rest api eg.localhost/api/contacts
    }

    public ArrayList<Contact> getContacts()
    {
        ArrayList<Contact> contacts;

        try
        {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection(); //creates a new http connection with the rest url
            conn.setRequestMethod("GET"); //sets the rest method to ger

            Reader reader = new InputStreamReader(conn.getInputStream()); //gets the data from the rest api
            GsonExclude exclude = new GsonExclude(); //used to speed serialization process

            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            contacts = gson.fromJson(reader, new TypeToken<List<Contact>>(){}.getType()); //gets a list of contacts from the api

        }catch(MalformedURLException mal){
            return null;
        }catch(IOException io){
            return null;
        }



        return contacts;
    }
}
