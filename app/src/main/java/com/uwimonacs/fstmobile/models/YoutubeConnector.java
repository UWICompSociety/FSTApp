package com.uwimonacs.fstmobile.models;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.uwimonacs.fstmobile.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;
    private final long MAX_RESULTS = 50;
    public static final String KEY = "AIzaSyDL2ZqeB3uGYmqv8jJgITwS4N9eFyBz4IA";
    public static final String CHANNEL_ID = "UCLSsrbKRNkPZF9_bTds6yAw";

    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {

            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setChannelId(CHANNEL_ID);
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
            query.setMaxResults(MAX_RESULTS);

        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e.getMessage());
        }
    }

    public List<VideoItem> search() {
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<>();

            for (SearchResult result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }

            return items;
        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }
}
