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
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhanelle on 6/22/2016.
 * ScholarshipActivity - not in use
 */
public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ScholarshipViewHolder> {

    private List<Scholarship> schols;

    /**
     * Initializes views for each item of the Recycler View items
     * using the Card View layout
     */
    public class ScholarshipViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView scholName;
        TextView scholDescription;
        ImageView scholPhoto;

        ScholarshipViewHolder(final View itemView) {
            super(itemView);

           // cv = (CardView)itemView.findViewById(R.id.cv);
            scholName = (TextView)itemView.findViewById(R.id.schol_name);
            scholDescription = (TextView)itemView.findViewById(R.id.schol_description);
            scholPhoto = (ImageView)itemView.findViewById(R.id.schol_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = ScholarshipViewHolder.this.getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ScholarshipDetailsActivity.class);
                    intent.putExtra("scholName", schols.get(pos).getTitle());
                    intent.putExtra("scholDetails", schols.get(pos).getDetail());
                   // intent.putExtra("scholImage", schols.get(pos).getImage());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schol_item, parent, false);
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
        final int pos = position;
        holder.scholName.setText(schols.get(position).getTitle());
        holder.scholDescription.setText(schols.get(position).getDescription());
        holder.scholPhoto.setImageResource(R.drawable.ic_school_black_24dp);


       /* holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mona.uwi.edu/osf/scholarship/list/fst")));
                Intent intent = new Intent(view.getContext(), ScholarshipDetailsActivity.class);
                intent.putExtra("scholName", schols.get(pos).getTitle());
                intent.putExtra("scholDetails", schols.get(pos).getDetail());
                intent.putExtra("scholImage", schols.get(pos).getImage());
                view.getContext().startActivity(intent);
            }
        });*/
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
    public void updateSchols(List<Scholarship> newSchols)
    {
        this.schols = new ArrayList<>(newSchols);
        notifyDataSetChanged();
    }

}
