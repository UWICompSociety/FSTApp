package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.AlertListAdapter;
import com.uwimonacs.fstmobile.models.Alert;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.sync.AlertSync;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;
import com.uwimonacs.fstmobile.util.DateTimeParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class QuickAlertsActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private List<Alert> alerts = new ArrayList<>();
    private RecyclerView alertList;
    private AlertListAdapter alertListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_alerts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_quick_alerts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        setUpSwipeRefresh();


        getAlertsFromDatabase();

        sortAlerts();

        //if there are new items present remove place holder image and text
        if (alerts.size() > 0) {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView();

        setUpProgressBar();

        new LoadAlertsTask(this).execute(); // runs the alerts sync task
    }

    private void initViews(){
        alertList = (RecyclerView) findViewById(R.id.alertList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView)findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void setUpRecyclerView() {
        alertList.setHasFixedSize(true);
        alertList.setLayoutManager(new LinearLayoutManager(this));
        alertListAdapter = new AlertListAdapter(this, alerts);
        alertList.setAdapter(alertListAdapter);

    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private void getAlertsFromDatabase() {
        alerts = new Select().all().from(Alert.class).execute();
    }

    private void sortAlerts()
    {
        Collections.sort(alerts,new Comparator<Alert>() {
            @Override
            public int compare(Alert lhs, Alert rhs) {
                if (DateTimeParser.convertDateTimeToMilliseconds(lhs.getDate()) < DateTimeParser.convertDateTimeToMilliseconds(rhs.getDate()))
                    return -1;
                else
                    return 1;
            }
        });
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
        //  do nothing for now
//        new LoadAlertsTask(this).execute();

    }

    private class LoadAlertsTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

        public LoadAlertsTask(Context ctxt)
        {
            this.ctxt = ctxt; // application context
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (alerts.size() == 0) { // check if any alerts are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final AlertSync alertSync = new AlertSync(Constants.ALERTS_URL);

            if (!isConnected()) { // if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            return alertSync.syncAlerts();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) { //if sync was successful
                getAlertsFromDatabase(); // get the freshly synced alerts from database

                sortAlerts();

                // update the list view to show new alerts
                alertListAdapter.updateAlertList(alerts);
            } else {
                // failed to sync maybe no internet or some error with api
                if (alerts.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
