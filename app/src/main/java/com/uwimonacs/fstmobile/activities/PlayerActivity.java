package com.uwimonacs.fstmobile.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.fragments.VideoFragment;


public class PlayerActivity extends AppCompatActivity {

    private TextView title, description;
    private VideoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.player_view_holder, VideoFragment.newInstance(getIntent().getStringExtra("VIDEO_ID")))
                .commit();
        title= (TextView) findViewById(R.id.player_view_title);
        description = (TextView) findViewById(R.id.player_view_description);
        title.setText(getIntent().getStringExtra("VIDEO_TITLE"));
        description.setText(getIntent().getStringExtra("VIDEO_DESC"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title.getText().toString());
        CardView watchOnYoutube = (CardView) findViewById(R.id.watch_on_youtube);
        watchOnYoutube.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + getIntent().getStringExtra("VIDEO_ID")));
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + getIntent().getStringExtra("VIDEO_ID")));
                    startActivity(intent);
                }
            }
        });
    }
}
