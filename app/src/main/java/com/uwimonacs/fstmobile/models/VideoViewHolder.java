package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

/**
 * Models the CardView item used in populating the RecyclerView
 * in VideoListActivity
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    public final ImageView vThumbnail;
    public final TextView vTitle, vDescription, vId;

    /**
     * Instantiates references to the sub-views of the CardView
     * given.
     * @param cardView the View to be passed to the RecyclerView
     */
    public VideoViewHolder(View cardView) {
        super(cardView);
        vThumbnail = (ImageView) cardView.findViewById(R.id.video_thumbnail);
        vTitle = (TextView) cardView.findViewById(R.id.video_title);
        vDescription = (TextView) cardView.findViewById(R.id.video_description);
        vId = (TextView) cardView.findViewById(R.id.video_id);
    }
}
