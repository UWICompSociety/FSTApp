package com.uwimonacs.fstmobile.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.mapActivity_MVP.MapActivity;
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
    private Toolbar toolbar;
    private View resultsView;
    private SearchResultsAdapter searchResultsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;

    private final String title = "Places";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
//        setContentView(R.layout.frag_places_search_results);

        initViews();

//        setUpSwipeRefresh();

        getPlacesFromDatabase();


//        if (places.size() > 0) { // if there are new items present remove place holder image and text
//            img_placeholder.setVisibility(View.GONE);
//            tv_placeholder.setVisibility(View.GONE);
//        }

        categories.setLayoutManager(new LinearLayoutManager(this));
        searchResultsView.setLayoutManager(new LinearLayoutManager(this));

        placesCategoriesAdapter = new PlacesCategoriesAdapter(this, this, places, categories);
        categories.setAdapter(placesCategoriesAdapter);
        searchResultsAdapter = new SearchResultsAdapter(this, this);
        searchResultsView.setAdapter(searchResultsAdapter);


        setUpToolBar();

//        setUpProgressBar();


        new PlacesCategoryActivity.LoadPlacesTask().execute();

        return;
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

    }

    public PlacesCategoryActivity() { /* required empty constructor */ }

    private void getPlacesFromDatabase(){
//        places = new Select().all().from(Place.class).execute();
        DbHelper dbHelper = AppDbHelper.getInstance(this);
        places = dbHelper.getLocations();
    }

//    @Override
//    public void onRefresh() {
//        new PlacesCategoryActivity.LoadPlacesTask().execute();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search,menu);

        setUpSearchView(menu);
        return true;
    }

    private void initViews()
    {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
//        tv_placeholder = (TextView) view.findViewById(R.id.txt_notpresent);
//        img_placeholder = (ImageView) view.findViewById(R.id.img_placeholder);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        categories = (RecyclerView) findViewById(R.id.activity_places_recyclerview);
        searchResultsView = (RecyclerView)  findViewById(R.id.activity_places_search_results_recyclerview);
    }

    private void setUpSearchView(Menu menu)
    {
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
        searchView.setQueryHint("Search Places");
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
                    newText = newText.toLowerCase();
                    final ArrayList<Place> searchResults = new ArrayList<>();
                    for (int i = 0; i < places.size(); i++) {
                        final String shortname = places.get(i).getId().toLowerCase();
                        final String fullname = places.get(i).getName().toLowerCase();
                        if ((shortname.contains(newText) || fullname.contains(newText)) && !fullname.contains("building"))
                            searchResults.add(places.get(i));
                    }
                    searchResultsAdapter.updateSearchResults(searchResults);
                    searchResultsView.setVisibility(View.VISIBLE);
                    categories.setVisibility(View.GONE);

                }
                return true;
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = findViewById(viewID);

        int width=myView.getWidth();

        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }


//    private void setUpSwipeRefresh() {
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
//                R.color.colorPrimaryDark);
//
//        swipeRefreshLayout.setOnRefreshListener(this);
//    }

//    private void setUpProgressBar() {
//        progressBar.setIndeterminate(true);
//        progressBar.setVisibility(View.GONE);
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search_view:
                circleReveal(R.id.menu_search_view,1,true,true);
                break;
            case android.R.id.home:
                onBackPressed();
                circleReveal(R.id.menu_search_view,1,true,false);
                return true;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
//            img_placeholder.setVisibility(View.GONE);
//            tv_placeholder.setVisibility(View.GONE);
//            if (places.size() == 0) { // check if any news are present
//                progressBar.setVisibility(View.VISIBLE);
//                if (swipeRefreshLayout.isRefreshing())
//                    progressBar.setVisibility(View.GONE);
//            }
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
//            swipeRefreshLayout.setRefreshing(false);
//            progressBar.setVisibility(View.GONE);
//            if (result) {
//                getPlacesFromDatabase();
//                placesCategoriesAdapter.updatePlaces(places);
//            }
//            else {
//                if (places.size() == 0) {
//                    img_placeholder.setVisibility(View.VISIBLE);
//                    tv_placeholder.setVisibility(View.VISIBLE);
                }
//            }
//        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
        finish();
    }
}

