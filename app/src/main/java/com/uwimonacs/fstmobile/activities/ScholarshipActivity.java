package com.uwimonacs.fstmobile.activities;

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

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ScholarshipAdapter;
import com.uwimonacs.fstmobile.models.Scholarship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhanelle on 6/22/2016.
 */

public class ScholarshipActivity extends AppCompatActivity {

    private List<Scholarship> schols;

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


        init();

        //Initializes and set adapter
        ScholarshipAdapter adapter = new ScholarshipAdapter(schols);
        rv.setAdapter(adapter);
    }

    /**
     * Initializes test data
     */
    private void init() {
        schols = new ArrayList<>();
        schols.add(new Scholarship(1, "AFUWI Scholarships", "Description", "detail"));
        schols.add(new Scholarship(2, "Ambassador Glen A. Holden Bursary", "Description", "detail"));
        schols.add(new Scholarship(3, "Digicel Scholarships", "Description", "detail"));
        schols.add(new Scholarship(4, "Douane Henry Memorial Bursary (The)", "Description", "detail"));
        schols.add(new Scholarship(5, "Jamaica Government Exhibition", "Description", "detail"));
        schols.add(new Scholarship(6, "Joe Pereira Scholarship (The)", "Description", "detail"));
        schols.add(new Scholarship(7, "Principal's Scholarship For Excellence (The)Digicel Scholarships", "Description", "detail"));
        schols.add(new Scholarship(8, "UWI Visa Card Scholarship", "Description", "detail"));
    }

}
