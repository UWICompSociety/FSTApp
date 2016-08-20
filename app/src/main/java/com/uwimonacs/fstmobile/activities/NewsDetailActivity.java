package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;

public class NewsDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Bundle extras;
    private String title;
    private String story;
    private String image_url;
    private ImageView newsHeader;
    private TextView newsTitle;
    private TextView newsStory;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsStory.setText(story);
        newsTitle.setText(title);
        Picasso.with(this).load(image_url).into(newsHeader);

        newsTitle.requestFocus(); //Prevents title scrolling offscreen
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        extras = getIntent().getExtras();
        title = extras.getString("title");
        story = extras.getString("story");
        image_url = extras.getString("image");
        newsHeader = (ImageView) findViewById(R.id.newsDetail_header);
        newsTitle = (TextView) findViewById(R.id.newsDetail_Topic);
        newsStory = (TextView) findViewById(R.id.newsStory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }
}
