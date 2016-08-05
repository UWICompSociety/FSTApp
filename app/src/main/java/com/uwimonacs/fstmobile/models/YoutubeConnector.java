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
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.uwimonacs.fstmobile.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {
    private YouTube.Search.List query;
    private static final long MAX_RESULTS = 50;

    /**
     * Handles setting up connection to YouTube Android API overhead.
     * Initializes a YouTube object and builds a search for video id, title,
     * description and thumbnail using the Faculty of Science and Technology's
     * YouTube channel id
     * @param context application context
     */
    public YoutubeConnector(Context context) {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {
            query = youtube.search().list("id,snippet"); //High level search categories
            query.setKey(context.getResources().getString(R.string.api_key));
            query.setType("video");
            query.setChannelId(context.getResources().getString(R.string.channel_id));
            //Low level search subcategories
            query.setFields(context.getResources().getString(R.string.required_fields));
            query.setMaxResults(MAX_RESULTS);

        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e.getMessage());
        }
    }

    /**
     * Executes search for videos using YouTube Android API and parses
     * the results to create VideoItem objects.
     * @return a List of VideoItems representing the videos on the playlist
     */
    public List<VideoItem> search() {
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<>();

            for (SearchResult result : results) {
                /*
                    Retrieve fields in the response from the API to build the
                    Item object and add it to the list above
                */
                VideoItem item = new VideoItem();
                SearchResultSnippet snippet = result.getSnippet();
                item.setTitle(snippet.getTitle());
                item.setDescription(snippet.getDescription());
                item.setThumbnailUrl(snippet.getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            Log.d("YoutubeConnector", "Could not search: " + e);
            return null;
        }
    }
}
