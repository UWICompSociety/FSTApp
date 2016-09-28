package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.BusRoutesAdapter;
import com.uwimonacs.fstmobile.models.AsyncResponse;
import com.uwimonacs.fstmobile.models.Bus;
import com.uwimonacs.fstmobile.models.BusTask;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class BusRoutesActivity extends AppCompatActivity
implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView busRoutes;
    private Toolbar toolbar;
    private BusRoutesAdapter adapter;
    private SwipeRefreshLayout swipe;
    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_routes);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_bus_routes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new BusTask(this, this).execute();

    }
    
    @Override
    public void onResume(){
        super.onResume();
        new BusTask(this, this).execute();
    }

    @Override
    public void processFinish(ArrayList<Bus> buses) {
        adapter.updateList(buses);
        if(swipe.isRefreshing())
            swipe.setRefreshing(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initViews(){
        adapter = new BusRoutesAdapter();
        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(this);
        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        busRoutes = (RecyclerView) findViewById(R.id.bus_routes);
        busRoutes.setLayoutManager(new LinearLayoutManager(this));
        busRoutes.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new BusTask(this, this).execute();
    }
}
