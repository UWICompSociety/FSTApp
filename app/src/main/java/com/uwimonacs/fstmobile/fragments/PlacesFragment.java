package com.uwimonacs.fstmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.PlacesCategoriesAdapter;
import com.uwimonacs.fstmobile.adapters.SearchResultsAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Room;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class PlacesFragment extends Fragment {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (view == null) // prevents crash on tab switch
        view = inflater.inflate(R.layout.frag_places, container, false); //inflates the layout for the view
        final View resultsView = inflater.inflate(R.layout.frag_places_search_results, container, false);
        final RecyclerView categories = (RecyclerView) view.findViewById(R.id.frag_places_recyclerview),
                searchResults = (RecyclerView) resultsView.findViewById(R.id.frag_places_search_results_recyclerview);
        categories.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));;
        categories.setAdapter(new PlacesCategoriesAdapter(getContext()));
        final SearchResultsAdapter adapter = new SearchResultsAdapter(getContext());
        searchResults.setAdapter(adapter);
        SearchView searchView = (SearchView) view.findViewById(R.id.frag_places_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do nothing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    ViewGroup rootView = (ViewGroup) getView();
                    assert rootView != null;
                    rootView.removeViewAt(1);
                    rootView.addView(categories);
                }
                else {
                    newText = newText.toLowerCase();
                    List<String> searchResults = new ArrayList<>();
                    for(int i=0; i<Constants.ROOMS.size(); i++){
                        String name = Constants.ROOMS.get(i).getName().toLowerCase();
                        if(name.contains(newText))
                            searchResults.add(Constants.ROOMS.get(i).getName());
                    }
                    adapter.updateSearchResults(searchResults);
                    ViewGroup rootView = (ViewGroup) getView();
                    assert rootView != null;
                    rootView.removeViewAt(1);
                    rootView.addView(resultsView);

                }
                return true;
            }
        });
        return view;
    }

    public PlacesFragment() { /* required empty constructor */ }
}
