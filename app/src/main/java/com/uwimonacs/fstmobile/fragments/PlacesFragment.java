package com.uwimonacs.fstmobile.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.PlacesCategoriesAdapter;
import com.uwimonacs.fstmobile.adapters.SearchResultsAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Place;
import com.uwimonacs.fstmobile.sync.PlaceSync;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class PlacesFragment extends Fragment {
    private View view;
    private List<Place> places;
    private String restUrl  = Constants.PLACE_URL;
    private PlacesCategoriesAdapter placesCategoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        getPlacesFromDatabase();
        if(places.size() == 0)
            initializePlaces();
        view = inflater.inflate(R.layout.frag_places, container, false); //inflates the layout for the view

        final View resultsView = inflater.inflate(R.layout.frag_places_search_results, container, false);

        final RecyclerView categories = (RecyclerView) view.findViewById(R.id.frag_places_recyclerview),
                searchResults = (RecyclerView) resultsView.findViewById(R.id.frag_places_search_results_recyclerview);

        categories.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        placesCategoriesAdapter = new PlacesCategoriesAdapter(getContext(), places);
        categories.setAdapter(placesCategoriesAdapter);
        final SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(getContext());
        searchResults.setAdapter(searchResultsAdapter);


        SearchView searchView = (SearchView) view.findViewById(R.id.frag_places_search);
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
                if(TextUtils.isEmpty(newText)){
                    ViewGroup rootView = (ViewGroup) getView();
                    assert rootView != null;
                    rootView.removeViewAt(1);
                    rootView.addView(categories);
                }
                /*
                * Search all the places for names that contain the text that has been typed.
                * Build a list of those names.
                * Pass it to an adapter and move that RecyclerView into the layout.
                * */
                else {
                    newText = newText.toLowerCase();
                    List<String> searchResults = new ArrayList<>();
                    for(int i=0; i<places.size(); i++){
                        String name = places.get(i).getShortname().toLowerCase();
                        if(name.contains(newText))
                            searchResults.add(places.get(i).getShortname());
                    }
                    searchResultsAdapter.updateSearchResults(searchResults);
                    ViewGroup rootView = (ViewGroup) getView();
                    assert rootView != null;
                    rootView.removeViewAt(1);
                    rootView.addView(resultsView);

                }
                return true;
            }
        });

        new LoadPlacesTask().execute();

        return view;
    }

    public PlacesFragment() { /* required empty constructor */ }

    private void getPlacesFromDatabase(){
        places = new Select().all().from(Place.class).execute();
    }

    private void initializePlaces(){
        //Fail safe in case database fetch fails
        Place place = new Place();
        places.add(place);
    }

    private class LoadPlacesTask extends AsyncTask<String,Integer,Boolean> {
        Context context;

        public LoadPlacesTask() {
            this.context = getContext();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context,"Loading Places..",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            PlaceSync placeSync = new PlaceSync(restUrl);
            boolean result = placeSync.syncPlaces();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                getPlacesFromDatabase();
                placesCategoriesAdapter.updatePlaces(places);
                Toast.makeText(context,"Places loaded",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context,"Places not loaded",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
