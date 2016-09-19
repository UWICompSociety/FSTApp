package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.BusRoutesAdapter;
import com.uwimonacs.fstmobile.models.AsyncResponse;
import com.uwimonacs.fstmobile.models.Bus;
import com.uwimonacs.fstmobile.models.BusTask;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class BusRoutesActivity extends AppCompatActivity
implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Bus> buses;
    private RecyclerView busRoutes;
    private Toolbar toolbar;
    private BusRoutesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_routes);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_bus_routes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();

        new BusTask(this, this).execute();

    }
    
    @Override
    public void onResume(){
        super.onResume();
        new BusTask(this, this).execute();
    }

    @Override
    public void processFinish(ArrayList<Bus> buses) {
        this.buses = buses;
        adapter.updateList(buses);
    }

    private void initViews(){
        buses = new ArrayList<>();
        adapter = new BusRoutesAdapter();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        busRoutes = (RecyclerView) findViewById(R.id.bus_routes);
    }

    private void setUpRecyclerView(){
        busRoutes.setLayoutManager(new LinearLayoutManager(this));
        busRoutes.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new BusTask(this, this).execute();
    }
}
