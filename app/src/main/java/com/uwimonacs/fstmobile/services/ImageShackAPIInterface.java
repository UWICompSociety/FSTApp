package com.uwimonacs.fstmobile.services;

import com.uwimonacs.fstmobile.models.ImageShackAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sylva on 7/28/2017.
 */

public interface ImageShackAPIInterface {


    @GET("albums/{id}")
    Call<ImageShackAlbum> getAlbum(@Path("id") String id);



}

