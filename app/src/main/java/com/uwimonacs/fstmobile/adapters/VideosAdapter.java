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
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.VideoViewHolder;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<VideoItem> videos = new ArrayList<>();
    private final Context context;
    private String filter = "";

    /**
     * Handles the setting up of the YouTube Android API
     * and executing a search to prepare a list of videos for
     * the RecyclerView
     * @param context
     */
    public VideosAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                *   Passes information to the Player activity so it can load the
                *   correct video set UI elements accordingly
                * */
                final TextView title = (TextView) v.findViewById(R.id.video_title),
                        description =  (TextView) v.findViewById(R.id.video_description),
                        id = (TextView) v.findViewById(R.id.video_id);
                final Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", id.getText().toString());
                intent.putExtra("VIDEO_TITLE", title.getText().toString());
                intent.putExtra("VIDEO_DESC", description.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final VideoItem item = videos.get(position);
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
        return videos.size();
    }

    public void updateVideos(List<VideoItem> new_videos) {
        this.videos = new ArrayList<>(new_videos);
        notifyDataSetChanged();
    }

    public void animateTo(List<VideoItem> models, String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.filter = filter;
    }

    private void applyAndAnimateRemovals(List<VideoItem> newModels) {
        for (int i = videos.size() - 1; i >= 0; i--) {
            final VideoItem model = videos.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<VideoItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final VideoItem model = newModels.get(i);
            if (!videos.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<VideoItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final VideoItem model = newModels.get(toPosition);
            final int fromPosition = videos.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public VideoItem removeItem(int position) {
        final VideoItem model = videos.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, VideoItem model) {
        videos.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final VideoItem model = videos.remove(fromPosition);
        videos.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
