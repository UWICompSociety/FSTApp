package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;


public class NewsDetailActivity extends AppCompatActivity {
    private String title;
    private String story;
    private String image_url;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("News");
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();

        title = extras.getString("title");
        story = extras.getString("story");
        image_url = extras.getString("image");
        url = extras.getString("url");

        final ImageView newsHeader = (ImageView) findViewById(R.id.newsDetail_header);
        final TextView newsTitle = (TextView) findViewById(R.id.newsDetail_Topic);
        final TextView newsStory = (TextView) findViewById(R.id.newsStory);

        newsTitle.setText(title);
        newsStory.setText(story);
        Picasso.with(this).load(image_url).into(newsHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }
}
