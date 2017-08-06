package com.uwimonacs.fstmobile.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sylva on 7/27/2017.
 */

public class ImageShackApiClient {

    private static final String API_KEY = "7PZNJSXV643ad3f3d03e668e46b50b05b2da20d8";
    private static final String BASE_URL = "https://api.imageshack.com/v2/";

    public static Retrofit  retrofit = null;


    public static Retrofit getAPIClient(){
        if(retrofit== null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }



}
