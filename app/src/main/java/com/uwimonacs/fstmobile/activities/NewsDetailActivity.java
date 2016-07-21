package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;

public class NewsDetailActivity extends AppCompatActivity {


    Bundle extras;
    private ImageView newsHeader;
    private TextView newsTitle;
    private TextView newsStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //functionality to go back to previous activity

        extras = getIntent().getExtras();

        String title = extras.getString("title");
        String story = extras.getString("story");
        String image_url = extras.getString("image");

        newsHeader = (ImageView)findViewById(R.id.newsDetail_header);
        newsTitle = (TextView)findViewById(R.id.newsDetail_Topic);
        newsStory = (TextView)findViewById(R.id.newsStory);

        newsTitle.setText(title);
        newsStory.setText(story);
        Picasso.with(this).load(image_url).into(newsHeader);






    }

}
