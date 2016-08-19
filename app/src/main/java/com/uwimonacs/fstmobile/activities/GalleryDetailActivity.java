package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.TouchImageView;

public class GalleryDetailActivity extends AppCompatActivity {
    private boolean fullscreen = false;
    private TextView tvTitle;
    private TouchImageView image;
    private Toolbar toolbar;
    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");

        getSupportActionBar().setTitle(title);
        String url = getIntent().getStringExtra("url");

        tvTitle = (TextView)findViewById(R.id.title);
        image = (TouchImageView)findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility();
            }
        });

        tvTitle.setText(title);
        Picasso.with(this).load(url).into(image);


    }

    private void toggleVisibility(){
        if(fullscreen)
            exitFullscreen();
        else
            enterFullscreen();
    }

    private void exitFullscreen(){
        appBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator())
                .setDuration(240)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().show();
                    }
                })
                .start();
        tvTitle.animate().translationY(0).setInterpolator(new DecelerateInterpolator())
                .setDuration(240).start();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fullscreen = false;
    }

    private void enterFullscreen(){
        appBar.animate().translationY(-appBar.getHeight()*2).setInterpolator(new AccelerateInterpolator())
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().hide();
                    }
                })
                .start();
        tvTitle.animate().translationY(-tvTitle.getHeight()*3).setInterpolator(new AccelerateInterpolator())
                .setDuration(240).start();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fullscreen = true;
    }

}
