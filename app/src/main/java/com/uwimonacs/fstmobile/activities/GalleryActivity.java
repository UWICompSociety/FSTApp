package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.GridViewAdapter;
import com.uwimonacs.fstmobile.models.ImageItem;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("FST Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_gallery_item, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(GalleryActivity.this, GalleryDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("url", item.getUrl());

                //Start details activity
                startActivity(intent);
            }
        });

    }


    private ArrayList getData()
    {
        ArrayList images = new ArrayList();
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2265.jpg","Image 1"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2266.jpg","Image 2"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2279.jpg","Image 3"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2285.jpg","Image 4"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2304.jpg","Image 5"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2308.jpg","Image 6"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2320.jpg","Image 7"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2323.jpg","Image 8"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2362.jpg","Image 9"));
        images.add(new ImageItem("https://sta.uwi.edu/fst/images/FST_Prizes_Ceremony_2015/FST_Prizes_2015_DSC_2338.jpg","Image 10"));

        return images;
    }

}
