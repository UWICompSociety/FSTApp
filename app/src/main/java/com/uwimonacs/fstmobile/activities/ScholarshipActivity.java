package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class ScholarshipActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<Scholarship> schols = new ArrayList<>();
    private ScholarshipAdapter adapter;
    private String url = Constants.SCHOL_URL;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Connect connect;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        initViews(); //initalize the views in the activity

        setUpToolBar(); //set up properties for toolbar such as title

        connect = new Connect(this); //used theck connectivity

        setUpSwipeRefresh(); //set up swipe down to refresh

        getScholsFromDatabase(); //get scholarships from database

        if(schols.size()>0) //if there are new items present remove place holder image and text
        {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView(); //set up recycler view

        setUpProgressBar(); //set up progress bar


        new LoadScholsTask(this).execute("");  //refresh items from internet
    }

    private void initViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        rv = (RecyclerView)findViewById(R.id.rv);
    }

    private void setUpToolBar()
    {
        setSupportActionBar(toolbar);

        //enable back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    private void setUpRecyclerView()
    {
        //Initializes and set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        //Initializes and set adapter
        adapter = new ScholarshipAdapter(schols);
        rv.setAdapter(adapter);
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

    private void getScholsFromDatabase() {
        schols = new Select().all().from(Scholarship.class).execute();
    }

    private boolean isConnected(){

        return connect.isConnected();
    }

    private boolean hasInternet()
    {
        boolean hasInternet;
        try{
            hasInternet =connect.haveInternetConnectivity();
        }catch(Exception e)
        {
            hasInternet = false;
        }

        return  hasInternet;

    }

    @Override
    public void onRefresh() {
        new LoadScholsTask(this).execute("");
    }

    private class LoadScholsTask extends AsyncTask<String,Integer,Boolean> {
        Context ctxt;

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
            ScholarshipSync scholSync = new ScholarshipSync(url);
            if(!isConnected())  //if there is no internet connection
            {
                return false;
            }

            if(!hasInternet()) //if there is no internet
            {
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
            }else {
                if(schols.size() == 0)
                {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }

    }



}
