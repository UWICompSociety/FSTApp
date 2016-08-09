package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.GridViewAdapter;
import com.uwimonacs.fstmobile.models.ImageItem;
import com.uwimonacs.fstmobile.sync.ImageSync;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private List images = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("FST Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        setUpSwipeRefresh();

        getImagesFromDatabase();

        if (images.size() > 0) { // if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }


        setUpGridView();


        setUpProgressBar();

        new LoadImagesTask(this).execute();


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


    private void getImagesFromDatabase()
    {
        images = new Select().all().from(ImageItem.class).execute();
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }


    private void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpGridView()
    {
        gridAdapter = new GridViewAdapter(this, R.layout.grid_gallery_item, images);
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

    @Override
    public void onRefresh() {
        new LoadImagesTask(this).execute();
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

    private class LoadImagesTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

        public LoadImagesTask(Context ctxt)
        {
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (images.size() == 0) { // check if any faqs are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final ImageSync imageSync = new ImageSync(Constants.IMAGES_URL);

            if (!isConnected()) { //if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            return imageSync.syncImages();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
                getImagesFromDatabase();
                gridAdapter = new GridViewAdapter(ctxt, R.layout.grid_gallery_item, images);
                gridView.setAdapter(gridAdapter);
            }
            else {
                if(images.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
