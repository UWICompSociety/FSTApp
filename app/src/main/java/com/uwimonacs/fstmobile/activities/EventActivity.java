package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.EventListAdapter;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.sync.ContactSync;
import com.uwimonacs.fstmobile.sync.EventSync;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private List<Event> events = new ArrayList<>();
    private RecyclerView eventList;
    private EventListAdapter eventListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();


        setUpSwipeRefresh();


        getEventsFromDatabase();


        //if there are new items present remove place holder image and text
        if (events.size() > 0) {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView();

        setUpProgressBar();

        new LoadEventsTask(this).execute(); // runs the events sync task

    }

    private void initViews() {
        eventList = (RecyclerView) findViewById(R.id.eventList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView)findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void setUpRecyclerView() {
        eventList.setHasFixedSize(true);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        eventListAdapter = new EventListAdapter(this, events);
        eventList.setAdapter(eventListAdapter);
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private void getEventsFromDatabase() {
        events = new Select().all().from(Event.class).execute();
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

        return hasInternet;
    }



    @Override
    public void onRefresh() {
        new LoadEventsTask(this).execute();
    }

    private class LoadEventsTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

        public LoadEventsTask(Context ctxt)
        {
            this.ctxt = ctxt; // application context
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (events.size() == 0) { // check if any events are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final EventSync eventSync = new EventSync(Constants.EVENTS_URL);

            if (!isConnected()) { // if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            return eventSync.syncEvents();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) { //if sync was successful
                getEventsFromDatabase(); // get the freshly synced events from database

                // update the list view to show new events
                eventListAdapter.updateEventList(events);
            } else {
                // failed to sync maybe no internet or some error with api
                if (events.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }




}
