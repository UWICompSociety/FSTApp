package com.uwimonacs.fstmobile.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.VideosAdapter;
import com.uwimonacs.fstmobile.helper.Connect;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{
    private RecyclerView videosFound;
    private VideosAdapter adapter;
    private List<VideoItem> videos = new ArrayList<>();
    private Connect connect;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private SearchView searchView;
    private Toolbar toolbar;
    private CardView videoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        initViews();

        setupReveal();

        setUpToolBar();

        setUpSwipeRefresh();

        setUpProgressBar();

        if (videos.size() > 0) {// if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        connect = new Connect(this);
        videosFound = (RecyclerView) findViewById(R.id.videos_found);

        setUpRecyclerView();

        new LoadVideosTask(this).execute();

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView) findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        videoCard = (CardView) findViewById(R.id.card_placeholder);
    }

    private void setupReveal(){
        if(Build.VERSION.SDK_INT >= 21) {
            videoCard.setVisibility(View.INVISIBLE);
            ViewTreeObserver observer = videoCard.getViewTreeObserver();
            if(observer.isAlive()){
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularReveal();
                        if(Build.VERSION.SDK_INT < 16)
                            videoCard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            videoCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    @SuppressLint("NewApi")
    private void circularReveal(){
        int cx = videoCard.getWidth() / 2;
        int cy = videoCard.getHeight() / 2;
        float finalRadius = (float) Math.max(videoCard.getWidth(), videoCard.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(videoCard, cx, cy, 0, finalRadius);
        anim.setDuration(1000);
        videoCard.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_videos);
    }

    private void setUpRecyclerView() {
        videosFound.setHasFixedSize(true);
        videosFound.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideosAdapter(getApplicationContext());
        videosFound.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new LoadVideosTask(this).execute();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_videos, menu);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search video"); //sets the hint text

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false); //clears text from search view
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<VideoItem> filteredModelList = filter(videos, query);
        adapter.animateTo(filteredModelList,query);
        videosFound.scrollToPosition(0);
        return true;
    }

    /**
     * takes a query and returns a list of contacts that match the query
     * @param models
     * @param query
     * @return
     */
    private List<VideoItem> filter(List<VideoItem> models, String query) {
        query = query.toLowerCase();
        final List<VideoItem> filteredModelList = new ArrayList<>();
        for (final VideoItem model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private boolean isConnected() {
        return connect.isConnected();
    }

    private boolean hasInternet()
    {
        boolean hasInternet;

        try {
            hasInternet = connect.haveInternetConnectivity();
        } catch(Exception e) {
            hasInternet = false;
        }

        return  hasInternet;

    }
    private class LoadVideosTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

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
        protected Boolean doInBackground(Void... params) {
            if (!isConnected()) { // if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            final YoutubeConnector yc = new YoutubeConnector(ctxt);
            videos = yc.search();

            if (videos == null) {
                return false;
            }

            if (videos.size() == 0) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
                adapter.updateVideos(videos);
            } else {
                if (videos.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
