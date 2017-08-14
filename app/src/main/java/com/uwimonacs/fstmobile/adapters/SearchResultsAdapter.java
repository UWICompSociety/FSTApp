package com.uwimonacs.fstmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PermissionsActivity;
import com.uwimonacs.fstmobile.models.locations.Place;


import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder>{
    private Activity activity;
    private ArrayList<Place> searchResults;
    private final Context context;

    public SearchResultsAdapter(Context context, Activity activity) {
        this.context = context;
        this.searchResults = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public SearchResultsAdapter.SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_search_results_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchResultsAdapter.SearchResultsHolder holder, final int position) {
        holder.result.setText(searchResults.get(position).getId());
        holder.fullname.setText(searchResults.get(position).getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Place place = searchResults.get(position);
                final Intent mapIntent = new Intent(v.getContext(), PermissionsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("places", searchResults);
                mapIntent.putExtra("location", place.getLL());
                mapIntent.putExtra("department", place.getCategory());
                mapIntent.putExtra("shortname", place.getId());
                mapIntent.putExtra("fullname", place.getName());
//                mapIntent.putExtra("placesList", bundle);

                v.getContext().startActivity(mapIntent);
                activity.overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
                activity.finish();
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
