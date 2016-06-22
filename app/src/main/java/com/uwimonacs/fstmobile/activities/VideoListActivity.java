package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.CardOnClickListener;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sultanofcardio on 6/21/16.
 */
public class VideoListActivity extends AppCompatActivity {
    private RecyclerView videosFound;
    private Handler handler;
    private List<VideoItem> videos, backupVideos;
    private CardOnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.videos_activity_title);
        onClickListener = new CardOnClickListener();
        videosFound = (RecyclerView) findViewById(R.id.videos_found);
        videosFound.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        videosFound.setLayoutManager(manager);
        handler = new Handler();
        searchOnYoutube();
    }

    private void searchOnYoutube() {
        new Thread(){
            @Override
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(getApplication());
                videos = yc.search();
                backupVideos = new ArrayList<>();
                VideoItem videoItem = new VideoItem();
                videoItem.setTitle("No Videos Found");
                videoItem.setDescription("No Videos Found");
                backupVideos.add(videoItem);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        RecyclerView.Adapter<VideoItem.VideoViewHolder> rvadapter = new RecyclerView.Adapter<VideoItem.VideoViewHolder>() {
            @Override
            public VideoItem.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
                itemView.setOnClickListener(onClickListener);
                return new VideoItem.VideoViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(VideoItem.VideoViewHolder holder, int position) {
                VideoItem item;
                if (videos.size() > 0)
                    item = videos.get(position);
                else
                    item = backupVideos.get(position);
                Picasso.with(getApplicationContext()).load(item.getThumbnailURL()).into(holder.vThumbnail);
                holder.vTitle.setText(item.getTitle());
                holder.vId.setText(item.getId());
                if (TextUtils.isEmpty(item.getDescription())) {
                    holder.vDescription.setText(item.getDescription());
                }
                else
                    holder.vDescription.setText(R.string.no_description);
            }

            @Override
            public int getItemCount() {
                if (videos.size() > 0)
                    return videos.size();
                else
                    return  backupVideos.size();
            }
        };
        videosFound.setAdapter(rvadapter);
    }
}
