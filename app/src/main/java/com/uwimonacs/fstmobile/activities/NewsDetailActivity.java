package com.uwimonacs.fstmobile.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

public class NewsDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private Bundle extras;
    private String title;
    private String story;
    private String image_url;
    private ImageView newsHeader;
    private TextView newsStory;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        appbarTitleBehaviour();

        newsStory.setText(story);
        Picasso.with(this).load(image_url).into(newsHeader);
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        extras = getIntent().getExtras();
        title = extras.getString("title");
        story = extras.getString("story");
        image_url = extras.getString("image");
        newsHeader = (ImageView) findViewById(R.id.newsDetail_header);
        newsStory = (TextView) findViewById(R.id.newsStory);
    }
    
    private void appbarTitleBehaviour(){
        toolbarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_Title);
        if(Build.VERSION.SDK_INT < 23)
            toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        else
            toolbarLayout.setExpandedTitleColor(getColor(android.R.color.white));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(toolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(toolbarLayout))
                    toolbarLayout.setTitle("News");
                else
                    toolbarLayout.setTitle(title);
            }
        });
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
