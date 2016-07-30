package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.VideosAdapter;
import com.uwimonacs.fstmobile.helper.Connect;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView videosFound;
    VideosAdapter adapter;
    List<VideoItem> videos = new ArrayList<>();
    private Connect connect;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        initViews();


        setUpToolBar();

        setUpSwipeRefresh();

        setUpProgressBar();

        if(videos.size()>0) //if there are new items present remove place holder image and text
        {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        connect = new Connect(this);
        videosFound = (RecyclerView) findViewById(R.id.videos_found);

        setUpRecyclerView();

        new LoadVideosTask(this).execute("");

    }

    private void initViews()
    {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void setUpSwipeRefresh()
    {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar()
    {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpToolBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.videos_activity_title);
    }

    private void setUpRecyclerView()
    {
        videosFound.setHasFixedSize(true);
        videosFound.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideosAdapter(getApplicationContext());
        videosFound.setAdapter(adapter);

    }

    @Override
    public void onRefresh() {
        new LoadVideosTask(this).execute("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return true;
        }
    }


    private boolean isConnected() {
        return connect.isConnected();
    }

    private boolean hasInternet()
    {
        boolean hasInternet = false;
        try {
            hasInternet = connect.haveInternetConnectivity();
        } catch(Exception e)
        {
            hasInternet = false;
        }

        return  hasInternet;

    }
    private class LoadVideosTask extends AsyncTask<String,Integer,Boolean>
    {
        Context ctxt;

        public LoadVideosTask(Context ctxt)
        {
            this.ctxt = ctxt;
        }
        @Override
        protected void onPreExecute() {

            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (videos.size() == 0) { // check if any faqs are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {


            if(!isConnected())  //if there is no internet connection
            {
                return false;
            }

            if(!hasInternet()) //if there is no internet
            {
                return false;
            }

            YoutubeConnector yc = new YoutubeConnector(ctxt);
            videos = yc.search();

            if(videos == null)
            {
                return false;
            }

            if(videos.size() == 0)
            {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if(result)
            {
                adapter.updateVideos(videos);
            }else{
                if(videos.size() == 0)
                {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }


        }
    }
}
