package com.uwimonacs.fstmobile.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.activities.ScholarshipDetailsActivity;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhanelle on 6/22/2016.
 * ScholarshipActivity - not in use
 */
public class ScholarshipAdapter
        extends RecyclerView.Adapter<ScholarshipAdapter.ScholarshipViewHolder> {
    private List<Scholarship> schols;
    private String filter = "";

    /**
     * Initializes views for each item of the Recycler View items
     * using the Card View layout
     */
    public class ScholarshipViewHolder extends RecyclerView.ViewHolder {
        final TextView scholName;
        final TextView scholDescription;

        ScholarshipViewHolder(final View v) {
            super(v);

            scholName = (TextView) v.findViewById(R.id.schol_name);
            scholDescription = (TextView) v.findViewById(R.id.schol_description);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = ScholarshipViewHolder.this.getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ScholarshipDetailsActivity.class);
                    intent.putExtra("scholName", schols.get(pos).getTitle());
                    intent.putExtra("scholDetails", schols.get(pos).getDetail());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    /**
     * Initializes the list of scholarship objects
     *
     * @param schols list of scholarship objects
     */
    public ScholarshipAdapter (List<Scholarship> schols) {
        this.schols = schols;
    }

    /**
     * Initializes the ScholarshipViewHolder
     * @return a ScholarshipViewHOlder object
     */
    @Override
    public ScholarshipViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        final View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_schol_item, parent, false);

        return new ScholarshipViewHolder(v);
    }


    /**
     * Sets the content of the ViewHolder using the data from the RecyclerView Items
     * Also specifies onClickListener for each card
     * @param holder a ScholarshipViewHolder object
     * @param position position in the list of scholarship objects
     */
    @Override
    public void onBindViewHolder(ScholarshipViewHolder holder, int position) {
        holder.scholName.setText(schols.get(position).getTitle());
        holder.scholDescription.setText(schols.get(position).getDescription());
    }

    /**
     * @return the number of items currently in the list of scholarship objects
     */
    @Override
    public int getItemCount() {
        return schols.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view) {
        super.onAttachedToRecyclerView(view);
    }

    /**
     * Updates the list of scholarships
     * @param newSchols updated list of scholarships
     */
    public void updateSchols(List<Scholarship> newSchols) {
        this.schols = new ArrayList<>(newSchols);
        notifyDataSetChanged();
    }

    public void animateTo(List<Scholarship> models, String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.filter = filter;
    }

    private void applyAndAnimateRemovals(List<Scholarship> newModels) {
        for (int i = schols.size() - 1; i >= 0; i--) {
            final Scholarship model = schols.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Scholarship> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Scholarship model = newModels.get(i);
            if (!schols.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Scholarship> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Scholarship model = newModels.get(toPosition);
            final int fromPosition = schols.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Scholarship removeItem(int position) {
        final Scholarship model = schols.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Scholarship model) {
        schols.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Scholarship model = schols.remove(fromPosition);
        schols.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
