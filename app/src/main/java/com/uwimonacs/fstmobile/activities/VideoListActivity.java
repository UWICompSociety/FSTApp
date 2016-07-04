package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.VideosAdapter;

public class VideoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.videos_activity_title);
        RecyclerView videosFound = (RecyclerView) findViewById(R.id.videos_found);
        videosFound.setHasFixedSize(true);
        videosFound.setLayoutManager(new LinearLayoutManager(this));
        VideosAdapter adapter = new VideosAdapter(getApplicationContext());
        videosFound.setAdapter(adapter);
    }
}
