package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PlayerActivity;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.VideoViewHolder;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<VideoItem> videos;
    private Context context;

    /**
     * Handles the setting up of the YouTube Android API
     * and executing a search to prepare a list of videos for
     * the RecyclerView
     * @param context
     */
    public VideosAdapter(final Context context){
        this.context = context;
        new Thread(){
            @Override
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(context);
                videos = yc.search();
            }
        }.start();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                *   Passes information to the Player activity so it can load the
                *   correct video set UI elements accordingly
                * */
                TextView title = (TextView) view.findViewById(R.id.video_title),
                        description =  (TextView) view.findViewById(R.id.video_description),
                        id = (TextView) view.findViewById(R.id.video_id);
                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", id.getText().toString());
                intent.putExtra("VIDEO_TITLE", title.getText().toString());
                intent.putExtra("VIDEO_DESC", description.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoItem item = videos.get(position);
        // Loads a thumbnail from its URL into the ImageView at bind time
        Picasso.with(context).load(item.getThumbnailURL()).into(holder.vThumbnail);
        holder.vTitle.setText(item.getTitle());
        holder.vId.setText(item.getId());
        //Provides a fallback string in case a video has no description
        if (TextUtils.isEmpty(item.getDescription())) {
            holder.vDescription.setText(item.getDescription());
        }
        else
            holder.vDescription.setText(R.string.no_description);
    }

    @Override
    public int getItemCount() {
        /*
        * Possible null pointer if YouTubeConnector takes too long
        * to return a list from yc.search() above. Pause the app until
        * results are confirmed to be returned.
        * Possible loading circle?
        * */
        while(videos == null)
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        return videos.size();
    }
}
