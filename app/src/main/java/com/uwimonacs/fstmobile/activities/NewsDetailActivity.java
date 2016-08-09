package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();

        final String title = extras.getString("title");
        final String story = extras.getString("story");
        final String image_url = extras.getString("image");

        final ImageView newsHeader = (ImageView) findViewById(R.id.newsDetail_header);
        final TextView newsTitle = (TextView) findViewById(R.id.newsDetail_Topic);
        final TextView newsStory = (TextView) findViewById(R.id.newsStory);

        newsTitle.setText(title);
        newsStory.setText(story);
        Picasso.with(this).load(image_url).into(newsHeader);
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
