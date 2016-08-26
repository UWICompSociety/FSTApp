package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.MapActivity;
import com.uwimonacs.fstmobile.activities.PermissionsActivity;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder>{
    private ArrayList<Place> searchResults;
    private final Context context;

    public SearchResultsAdapter(Context context) {
        this.context = context;
        this.searchResults = new ArrayList<>();
    }

    @Override
    public SearchResultsAdapter.SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_search_results_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchResultsAdapter.SearchResultsHolder holder, final int position) {
        holder.result.setText(searchResults.get(position).getShortname());
        holder.fullname.setText(searchResults.get(position).getFullname());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Place place = searchResults.get(position);
                final Intent mapIntent = new Intent(v.getContext(), PermissionsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("places", searchResults);
                mapIntent.putExtra("location", place.getLocation());
                mapIntent.putExtra("department", place.getDepartment());
                mapIntent.putExtra("shortname", place.getShortname());
                mapIntent.putExtra("fullname", place.getFullname());
                mapIntent.putExtra("placesList", bundle);
                v.getContext().startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateSearchResults(ArrayList<Place> searchResults){
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    public static class SearchResultsHolder extends RecyclerView.ViewHolder{
        public final TextView result;
        public final TextView fullname;
        public final CardView cardView;

        public SearchResultsHolder(View v) {
            super(v);

            cardView = (CardView) v;
            result = (TextView) v.findViewById(R.id.frag_places_search_results_item_category_name);
            fullname = (TextView)v.findViewById(R.id.frag_places_search_results_item_category_fullname);
        }
    }
}
