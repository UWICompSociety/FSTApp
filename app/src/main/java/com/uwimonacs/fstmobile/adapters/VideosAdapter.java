package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PlayerActivity;
import com.uwimonacs.fstmobile.models.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<VideoItem> mVideos = new ArrayList<>();
    private final Context mContext;
    private String mFilter = "";

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public final ImageView vThumbnail;
        public final TextView vTitle;

        public VideoViewHolder(View v) {
            super(v);

            vThumbnail = (ImageView) v.findViewById(R.id.video_thumbnail);
            vTitle = (TextView) v.findViewById(R.id.video_title);
        }
    }

    /**
     * Handles the setting up of the YouTube Android API
     * and executing a search to prepare a list of videos for
     * the RecyclerView
     * @param context
     */
    public VideosAdapter(final Context context) {
        this.mContext = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder vh, int position) {
        final VideoItem item = mVideos.get(position);
        // Loads a thumbnail from its URL into the ImageView at bind time
        Picasso.with(mContext).load(item.getThumbnailUrl()).into(vh.vThumbnail);
        vh.vTitle.setText(item.getTitle());

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", item.getId());
                intent.putExtra("VIDEO_TITLE", item.getTitle());

                String desc = item.getDescription();
                //Provides a fallback string in case a video has no description
                if (TextUtils.isEmpty(desc)) {
                    desc = mContext.getText(R.string.no_description).toString();
                }

                intent.putExtra("VIDEO_DESC", desc);
                v.getContext().startActivity(intent);
            }
        });

        // Display the overlay if the video has already been played.
        if (item.getAlreadyPlayed()) {
            styleVideoAsViewed(vh.itemView);
        }
    }

    /**
     * Show the video as Viewed. We display a semi-transparent grey overlay over the video
     * thumbnail.
     */
    private void styleVideoAsViewed(View videoItemView) {
        ImageView thumbnailView = (ImageView) videoItemView.findViewById(R.id.video_thumbnail);
        thumbnailView.setColorFilter(ContextCompat.getColor(mContext, R.color.video_scrim_watched));
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void updateVideos(List<VideoItem> new_videos) {
        this.mVideos = new ArrayList<>(new_videos);
        notifyDataSetChanged();
    }

    public void animateTo(List<VideoItem> models, String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.mFilter = filter;
    }

    private void applyAndAnimateRemovals(List<VideoItem> newModels) {
        for (int i = mVideos.size() - 1; i >= 0; i--) {
            final VideoItem model = mVideos.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<VideoItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final VideoItem model = newModels.get(i);
            if (!mVideos.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<VideoItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final VideoItem model = newModels.get(toPosition);
            final int fromPosition = mVideos.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public VideoItem removeItem(int position) {
        final VideoItem model = mVideos.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, VideoItem model) {
        mVideos.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final VideoItem model = mVideos.remove(fromPosition);
        mVideos.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
