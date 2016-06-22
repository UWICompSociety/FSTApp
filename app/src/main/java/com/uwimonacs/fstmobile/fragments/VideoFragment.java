package com.uwimonacs.fstmobile.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

/**
 * Created by sultanofcardio on 6/19/16.
 */
public class VideoFragment extends YouTubePlayerSupportFragment{

    public static VideoFragment newInstance(String url) {

        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_ID", url);

        fragment.setArguments(bundle);
        fragment.initialize();

        return fragment;
    }

    private void initialize() {

        initialize(YoutubeConnector.KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if(!b) {
                    youTubePlayer.loadVideo(getArguments().getString("VIDEO_ID"));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getContext(), "Initialization Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}