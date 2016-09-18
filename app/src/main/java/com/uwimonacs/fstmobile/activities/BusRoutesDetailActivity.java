package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.BusRoutesAdapter;
import com.uwimonacs.fstmobile.adapters.BusRoutesDetailAdapter;
import com.uwimonacs.fstmobile.models.AsyncResponse;
import com.uwimonacs.fstmobile.models.Bus;
import com.uwimonacs.fstmobile.models.BusTask;
import com.uwimonacs.fstmobile.util.DividerItemDecoration;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class BusRoutesDetailActivity extends AppCompatActivity {
    private String busTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private BusRoutesDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_routes_detail);

        busTitle = getIntent().getExtras().getString("title");

        initViews();

        initToolbar();

        setUpRecyclerView();
    }

    private void initViews(){
        adapter = new BusRoutesDetailAdapter(this, busTitle.substring(busTitle.length()-1));
        recyclerView = (RecyclerView) findViewById(R.id.bus_routes_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(busTitle);
        getSupportActionBar().setSubtitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
//        adapter.updateList(busTitle.substring(busTitle.length()-1));
    }
}
