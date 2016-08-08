package com.uwimonacs.fstmobile.activities;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.VideosAdapter;
import com.uwimonacs.fstmobile.models.VideoItem;
import com.uwimonacs.fstmobile.models.YoutubeConnector;
import com.uwimonacs.fstmobile.util.DividerItemDecoration;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{
    private RecyclerView videosFound;
    private VideosAdapter adapter;
    private List<VideoItem> videos = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private SearchView searchView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        initViews();

        setUpToolBar();

        setUpSwipeRefresh();

        setUpProgressBar();

        if (videos.size() > 0) {// if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        videosFound = (RecyclerView) findViewById(R.id.videos_found);

        setUpRecyclerView();

        final LoadVideosTask task = new LoadVideosTask(this,
                swipeRefreshLayout,
                progressBar,
                img_placeholder,
                tv_placeholder,
                videos,
                adapter);

        task.execute();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView) findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
        videosFound.addItemDecoration(new DividerItemDecoration(this));
        videosFound.setHasFixedSize(true);
        videosFound.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideosAdapter(getApplicationContext());
        videosFound.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        final LoadVideosTask task = new LoadVideosTask(this,
                swipeRefreshLayout,
                progressBar,
                img_placeholder,
                tv_placeholder,
                videos,
                adapter);

        task.execute();
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

    private class LoadVideosTask extends AsyncTask<Void,Void,Boolean> {
        private final WeakReference<Context> mCtxtRef;
        private final WeakReference<ImageView> mImageViewRef;
        private final WeakReference<TextView> mTextViewRef;
        private WeakReference<List<VideoItem>> mVideosRef;
        private final WeakReference<SwipeRefreshLayout> mSwipeRefreshLayoutRef;
        private final WeakReference<ProgressBar> mProgressBarRef;
        private final WeakReference<VideosAdapter> mVideosAdapterRef;

        public LoadVideosTask(Context ctxt,
                              SwipeRefreshLayout swipeRefreshLayout,
                              ProgressBar progressBar,
                              ImageView iv,
                              TextView tv,
                              List<VideoItem> videos,
                              VideosAdapter adapter) {
            mCtxtRef = new WeakReference<>(ctxt);
            mSwipeRefreshLayoutRef = new WeakReference<>(swipeRefreshLayout);
            mProgressBarRef = new WeakReference<>(progressBar);
            mImageViewRef = new WeakReference<>(iv);
            mTextViewRef = new WeakReference<>(tv);
            mVideosRef = new WeakReference<>(videos);
            mVideosAdapterRef = new WeakReference<>(adapter);
        }

        @Override
        protected void onPreExecute() {
            mImageViewRef.get().setVisibility(View.GONE);
            mTextViewRef.get().setVisibility(View.GONE);

            if (mVideosRef.get().size() == 0) { // check if any videos are present
                mProgressBarRef.get().setVisibility(View.VISIBLE);
                if (mSwipeRefreshLayoutRef.get().isRefreshing())
                    mProgressBarRef.get().setVisibility(View.GONE);
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

            final Context context = mCtxtRef.get();
            if (context != null) {
                final YoutubeConnector yc = new YoutubeConnector(context);

                videos = mVideosRef.get();
                if (videos != null) {
                    videos = yc.search();
                    mVideosRef.clear();
                    mVideosRef = new WeakReference<>(videos);

                    if (videos == null) {
                        return false;
                    }

                    if (videos.size() == 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (isCancelled()) {
                return;
            }

            final SwipeRefreshLayout swipeRefreshLayout = mSwipeRefreshLayoutRef.get();
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }

            final ProgressBar progressBar = mProgressBarRef.get();
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            if (result) {
                final VideosAdapter adapter = mVideosAdapterRef.get();
                if (adapter != null) {
                    videos = mVideosRef.get();
                    if (videos != null) {
                        adapter.updateVideos(videos);
                    }
                }
            } else {
                videos = mVideosRef.get();
                if (videos != null) {
                    if (videos.size() == 0) {
                        final ImageView imageView = mImageViewRef.get();
                        if (imageView != null) {
                            imageView.setVisibility(View.VISIBLE);
                        }

                        final TextView textView = mTextViewRef.get();
                        if (textView != null) {
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

        private boolean isConnected() {
            final Context context = mCtxtRef.get();
            if (context != null) {
                final ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            } else {
                return false;
            }
        }

        private  boolean hasInternet() {
            boolean result;

            try {
                final URL url = new URL("http://www.google.com");

                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200)
                    result = true;   // internet is present
                else {
                    result = false;  // no internet present
                }
            } catch (Exception e) {
                result = false;
            }

            return result;
        }
    }
}
