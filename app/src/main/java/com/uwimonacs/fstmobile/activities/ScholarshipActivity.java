package com.uwimonacs.fstmobile.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ScholarshipAdapter;
import com.uwimonacs.fstmobile.helper.Connect;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.sync.ScholarshipSync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhanelle on 6/22/2016.
 * ScholarshipActivity - not in use
 */

public class ScholarshipActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private List<Scholarship> schols = new ArrayList<>();
    private ScholarshipAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Connect connect;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView rv;
    private SearchView searchView;
    private CardView root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        initViews(); // initalize the views in the activity

        setupReveal();

        setUpToolBar(); // set up properties for toolbar such as title

        connect = new Connect(this); // used to heck connectivity

        setUpSwipeRefresh(); // set up swipe down to refresh

        getScholsFromDatabase(); // get scholarships from database

        if (schols.size() > 0) { // if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView();

        setUpProgressBar();

        new LoadScholsTask(this).execute("");  // refresh items from internet
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView) findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rv = (RecyclerView) findViewById(R.id.rv);
        root = (CardView) findViewById(R.id.root);
    }

    private void setupReveal(){
        if(Build.VERSION.SDK_INT >= 21) {
            root.setVisibility(View.INVISIBLE);
            ViewTreeObserver observer = root.getViewTreeObserver();
            if(observer.isAlive()){
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularReveal();
                        if(Build.VERSION.SDK_INT < 16)
                            root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    @SuppressLint("NewApi")
    private void circularReveal(){
        int cx = root.getWidth() / 2;
        int cy = root.getHeight() / 2;
        float finalRadius = (float) Math.max(root.getWidth(), root.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(root, cx, cy, 0, finalRadius);
        anim.setDuration(1000);
        root.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void setUpRecyclerView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScholarshipAdapter(schols);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
        inflater.inflate(R.menu.menu_scholarship, menu);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search scholarship"); //sets the hint text

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
        final List<Scholarship> filteredModelList = filter(schols, query);
        adapter.animateTo(filteredModelList,query);
        rv.scrollToPosition(0);
        return true;
    }

    /**
     * takes a query and returns a list of contacts that match the query
     * @param models
     * @param query
     * @return
     */
    private List<Scholarship> filter(List<Scholarship> models, String query) {
        query = query.toLowerCase();
        final List<Scholarship> filteredModelList = new ArrayList<>();
        for (final Scholarship model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    /**
     * Gets the list of scholarships from the database
     */
    private void getScholsFromDatabase() {
        schols = new Select().all().from(Scholarship.class).execute();
    }

    private boolean isConnected(){

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

    /**
     * Loads and updates list of scholarships
     */
    @Override
    public void onRefresh() {
        new LoadScholsTask(this).execute("");
    }

    private class LoadScholsTask extends AsyncTask<String,Integer,Boolean> {
        final Context ctxt;

        public LoadScholsTask(Context ctxt) {
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (schols.size() == 0) { // check if any faqs are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final ScholarshipSync scholSync = new ScholarshipSync(Constants.SCHOL_URL);

            if (!isConnected()) { // if there is no internet connection
                return false;
            }

            if (!hasInternet()) {// if there is no internet
                return false;
            }

            return scholSync.syncSchol();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
                getScholsFromDatabase();
                adapter.updateSchols(schols);
            } else {
                if (schols.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
