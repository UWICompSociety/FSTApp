package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.helper.GsonExclude;
import com.uwimonacs.fstmobile.models.Scholarship;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestScholarship {
    private String url = "";

    public RestScholarship(String url)
    {
        this.url = url;
    }

    public ArrayList<Scholarship> getSchols()
    {
        ArrayList<Scholarship> schols;

        try
        {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");

            Reader reader = new InputStreamReader(conn.getInputStream()); //gets the data
            GsonExclude exclude = new GsonExclude(); //used to speed up

            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            //converts the json to  a list of cars
            schols = gson.fromJson(reader, new TypeToken<List<Scholarship>>(){}.getType());
        }

        catch(MalformedURLException mal)
        {
            return null;
        }
        catch(IOException io)
        {
            return null;
        }

        return schols;
    }
}
