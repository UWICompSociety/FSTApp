package com.uwimonacs.fstmobile.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.helper.GsonExclude;
import com.uwimonacs.fstmobile.models.FAQ;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestFAQ {

    String url = "";

    public RestFAQ(String url)
    {
        this.url = url; //url to to connect to backend api
    }

    public ArrayList<FAQ> getFAQs()
    {
        ArrayList<FAQ> faqs;

        try
        {
            //Create http request and set to GET
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");

            Reader reader = new InputStreamReader(conn.getInputStream()); //gets the data
            GsonExclude exclude = new GsonExclude(); //used to speed up

            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();

            //converts the json to  a list of cars
            faqs = gson.fromJson(reader, new TypeToken<List<FAQ>>(){}.getType());
        }

        catch(MalformedURLException mal)
        {
            return null;
        }
        catch(IOException io)
        {
            return null;
        }

        return faqs;
    }
}
