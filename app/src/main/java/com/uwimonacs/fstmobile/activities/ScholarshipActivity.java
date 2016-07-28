package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ScholarshipAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.sync.ScholarshipSync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhanelle on 6/22/2016.
 * ScholarshipActivity - not in use
 */

public class ScholarshipActivity extends AppCompatActivity {

    private List<Scholarship> schols = new ArrayList<>();
    private ScholarshipAdapter adapter;
    private String url = Constants.SCHOL_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);

        //Initializes and set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        //init();
        getScholsFromDatabase();

        //Initializes and set adapter
        adapter = new ScholarshipAdapter(schols);
        rv.setAdapter(adapter);

        new LoadScholsTask(this).execute();
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

    private class LoadScholsTask extends AsyncTask<String,Integer,Boolean> {
        Context ctxt;

        public LoadScholsTask(Context ctxt) {
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt, "Loading Scholarships and Bursaries...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ScholarshipSync scholSync = new ScholarshipSync(url);
            boolean result = scholSync.syncSchol();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                getScholsFromDatabase();
                Toast.makeText(ctxt, "Scholarships loaded successfully",Toast.LENGTH_SHORT).show();
                adapter.updateSchols(schols);
            }else {
                Toast.makeText(ctxt,"Failed",Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Initializes test data
     *//*
    private void init() {
        schols = new ArrayList<>();
        schols.add(new Scholarship(1, "AFUWI Scholarships", "Description", "detail-1"));
        schols.add(new Scholarship(2, "Ambassador Glen A. Holden Bursary", "Description", "detail-2"));
        schols.add(new Scholarship(3, "Digicel Scholarships", "Description", "detail-3"));
        schols.add(new Scholarship(4, "Douane Henry Memorial Bursary (The)", "Description", "detail-4"));
        schols.add(new Scholarship(5, "Jamaica Government Exhibition", "Description", "detail-5"));
        schols.add(new Scholarship(6, "Joe Pereira Scholarship (The)", "Description", "detail-6"));
        schols.add(new Scholarship(7, "Principal's Scholarship For Excellence (The)Digicel Scholarships", "Description", "detail-7"));
        schols.add(new Scholarship(8, "UWI Visa Card Scholarship", "Description", "detail-8"));
    }*/

}
