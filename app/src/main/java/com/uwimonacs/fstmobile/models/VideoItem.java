package com.uwimonacs.fstmobile.models;

/**
 * Models a YouTube video for playing in a YouTubePlayer
 * @author sultanofcardio
 */
public class VideoItem {

    private String mId;

    private String mTitle;

    private String mDesc;

    private String mThumbnailURL;

    private boolean mAlreadyPlayed = false;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDesc;
    }

    public void setDescription(String description) {
        this.mDesc = description;
    }

    public String getThumbnailUrl() {
        return mThumbnailURL;
    }

    public void setThumbnailUrl(String thumbnailURL) {
        this.mThumbnailURL = thumbnailURL;
    }

    public boolean getAlreadyPlayed() {
        return mAlreadyPlayed;
    }

    public void setAlreadyPlayed(boolean alreadyPlayed) {
        mAlreadyPlayed = alreadyPlayed;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
