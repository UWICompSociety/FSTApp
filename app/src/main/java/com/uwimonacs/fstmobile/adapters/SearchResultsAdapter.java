package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder>{
    private List<String> searchResults;
    private Context context;

    public SearchResultsAdapter(Context context){
        this.context = context;
        searchResults = new ArrayList<>();
    }

    @Override
    public SearchResultsAdapter.SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_search_results_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchResultsAdapter.SearchResultsHolder holder, int position) {
        holder.result.setText(searchResults.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked "+
                        searchResults.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                //TODO: Start MapActivity here
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateSearchResults(List<String> searchResults){
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    class SearchResultsHolder extends RecyclerView.ViewHolder{
        public TextView result;
        public CardView cardView;
        public SearchResultsHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            result = (TextView) itemView.findViewById(R.id.frag_places_search_results_item_category_name);
        }
    }

}
