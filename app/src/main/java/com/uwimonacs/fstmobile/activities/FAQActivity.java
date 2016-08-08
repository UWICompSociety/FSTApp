package com.uwimonacs.fstmobile.activities;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.FaqListAdapter;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;
import com.uwimonacs.fstmobile.models.FAQ;
import com.uwimonacs.fstmobile.sync.FAQSync;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private List<FAQ> faqs;
    private FaqListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView faqList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initViews();

        setUpToolBar();

        setUpSwipeRefresh();

        getFAQsFromDatabase();

        if (faqs.size() > 0) { // if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView();

        setUpProgressBar();

        new LoadFAQsTask(this).execute();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAQs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        faqList = (RecyclerView)findViewById(R.id.faqlist);
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
        faqList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FaqListAdapter(faqs);
        faqList.setAdapter(adapter);
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
        // Inflate the options menu from XML
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_faq, menu);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Ask a question"); //sets the hint text

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
        final List<FAQ> filteredModelList = filter(faqs, query);
        adapter.animateTo(filteredModelList, query);
        faqList.scrollToPosition(0);
        return true;
    }

    private void getFAQsFromDatabase()
    {
        faqs = new Select().all().from(FAQ.class).execute();
    }

    private boolean isConnected() {
        return ConnectUtils.isConnected(this);
    }

    private static boolean hasInternet() {
        boolean hasInternet;

        try {
            hasInternet = ConnectUtils.haveInternetConnectivity();
        } catch(Exception e) {
            hasInternet = false;
        }

        return  hasInternet;

    }

    /**
     * takes a query and returns a list of faqs that match the query
     * @param models
     * @param query
     * @return
     */
    private List<FAQ> filter(List<FAQ> models, String query) {
        query = query.toLowerCase();
        final List<FAQ> filteredModelList = new ArrayList<>();

        for (final FAQ model : models) {
            final String text = model.getQuestion().toLowerCase();

            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }

    @Override
    public void onRefresh() {
        new LoadFAQsTask(this).execute();
    }

    private class LoadFAQsTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

        public LoadFAQsTask(Context ctxt)
        {
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (faqs.size() == 0) { // check if any faqs are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final FAQSync faqSync = new FAQSync(Constants.FAQS_URL);

            if (!isConnected()) { //if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            return faqSync.syncFAQs();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
                getFAQsFromDatabase();
                adapter.updateFAQs(faqs);
            }
            else {
                if(faqs.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
