package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.PlacesCategoriesAdapter;
import com.uwimonacs.fstmobile.adapters.SearchResultsAdapter;
import com.uwimonacs.fstmobile.data.AppDbHelper;
import com.uwimonacs.fstmobile.data.DbHelper;
import com.uwimonacs.fstmobile.fragments.PlacesFragment;

import com.uwimonacs.fstmobile.models.locations.Place;
import com.uwimonacs.fstmobile.sync.PlaceSync;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylva on 8/8/2017.
 */

public class PlacesCategoryActivity extends AppCompatActivity {



    private View view;
    private List<Place> places = new ArrayList<>();
    private PlacesCategoriesAdapter placesCategoriesAdapter;
    private RecyclerView categories;
    private RecyclerView searchResultsView;
    private View resultsView;
    private SearchResultsAdapter searchResultsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        initViews();

//        setUpSwipeRefresh();

        getPlacesFromDatabase();

        if (places.size() > 0) { // if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        categories.setLayoutManager(new LinearLayoutManager(this));
        searchResultsView.setLayoutManager(new LinearLayoutManager(this));

        placesCategoriesAdapter = new PlacesCategoriesAdapter(this, places, categories);
        categories.setAdapter(placesCategoriesAdapter);
        searchResultsAdapter = new SearchResultsAdapter(this);
        searchResultsView.setAdapter(searchResultsAdapter);

        setUpSearchView();

        setUpProgressBar();


        new PlacesCategoryActivity.LoadPlacesTask().execute();

        return;
    }

    public PlacesCategoryActivity() { /* required empty constructor */ }

    private void getPlacesFromDatabase(){
//        places = new Select().all().from(Place.class).execute();

        DbHelper dbHelper = AppDbHelper.getInstance(this);
        dbHelper.getLocations();


    }

//    @Override
//    public void onRefresh() {
//        new PlacesCategoryActivity.LoadPlacesTask().execute();
//    }

    private void initViews()
    {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
//        tv_placeholder = (TextView) view.findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) view.findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        categories = (RecyclerView) view.findViewById(R.id.frag_places_recyclerview);
        searchResultsView = (RecyclerView) view.findViewById(R.id.frag_places_search_results_recyclerview);
    }

    private void setUpSearchView()
    {
        final SearchView searchView = (SearchView) view.findViewById(R.id.frag_places_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do nothing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*
                * Move the original RecyclerView with the categories into the layout
                * if the search bar is empty
                * */
                if (TextUtils.isEmpty(newText)) {
                    searchResultsView.setVisibility(View.GONE);
                    categories.setVisibility(View.VISIBLE);
                }
                /*
                * Search all the places for names that contain the text that has been typed.
                * Build a list of those names.
                * Pass it to an adapter and move that RecyclerView into the layout.
                * */
                else {
//                    newText = newText.toLowerCase();
//                    final ArrayList<Place> searchResults = new ArrayList<>();
//                    for (int i = 0; i < places.size(); i++) {
//                        final String shortname = places.get(i).getShortname().toLowerCase();
//                        final String fullname = places.get(i).getFullname().toLowerCase();
//                        if ((shortname.contains(newText) || fullname.contains(newText)) && !fullname.contains("building"))
//                            searchResults.add(places.get(i));
//                    }
//                    searchResultsAdapter.updateSearchResults(searchResults);
//                    searchResultsView.setVisibility(View.VISIBLE);
//                    categories.setVisibility(View.GONE);

                }
                return true;
            }
        });
    }

//    private void setUpSwipeRefresh() {
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
//                R.color.colorPrimaryDark);
//
//        swipeRefreshLayout.setOnRefreshListener(this);
//    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private boolean isConnected() {
        return ConnectUtils.isConnected(this);
    }

    private static boolean hasInternet()
    {
        boolean hasInternet;

        try {
            hasInternet = ConnectUtils.haveInternetConnectivity();
        } catch(Exception e) {
            hasInternet = false;
        }

        return  hasInternet;
    }

    private class LoadPlacesTask extends AsyncTask<Void,Void,Boolean> {
        final Context context;

        public LoadPlacesTask() {
            this.context = PlacesCategoryActivity.this;
        }

        @Override
        protected void onPreExecute() {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
            if (places.size() == 0) { // check if any news are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final PlaceSync placeSync = new PlaceSync(Constants.PLACE_URL);

            if (!isConnected()) {  // if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }
            return placeSync.syncPlaces();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) {
                getPlacesFromDatabase();
                placesCategoriesAdapter.updatePlaces(places);
            }
            else {
                if (places.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}

