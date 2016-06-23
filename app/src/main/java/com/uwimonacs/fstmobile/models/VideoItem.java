package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class VideoItem {

    private String title;
    private String description;
    private String thumbnailURL;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        public ImageView vThumbnail;
        public TextView vTitle, vDescription, vId;

        public VideoViewHolder(View itemView) {
            super(itemView);
            vThumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
            vTitle = (TextView) itemView.findViewById(R.id.video_title);
            vDescription = (TextView) itemView.findViewById(R.id.video_description);
            vId = (TextView) itemView.findViewById(R.id.video_id);
        }
    }
}