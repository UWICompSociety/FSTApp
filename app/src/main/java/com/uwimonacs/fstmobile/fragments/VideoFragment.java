package com.uwimonacs.fstmobile.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

public class VideoFragment extends YouTubePlayerSupportFragment{

    public static VideoFragment newInstance(String url) {

        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_ID", url);

        fragment.setArguments(bundle);
        fragment.initialize();

        return fragment;
    }

    /**
     * Abstracts the initialization of the YouTube video found at the URL given in
     * newInstance(). A YouTubePLayer.OnInitializedListener starts loading the video
     * as soon as initialization is complete.
     */
    private void initialize() {

        initialize(getActivity().getApplicationContext()
                .getResources().getString(R.string.channel_id), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if(!b) {
                    /*
                    * Used in place of youTubePlayer.cueVideo() so that the
                    * video will play automatically
                    * */
                    youTubePlayer.loadVideo(getArguments().getString("VIDEO_ID"));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getContext(), "Initialization Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
