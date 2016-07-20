package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesCategoriesAdapter extends RecyclerView.Adapter<PlacesCategoriesAdapter.PlacesViewHolder> {
    private Context context;
    private List<Place> places;
    List<String> departments;

    public PlacesCategoriesAdapter(Context context, List<Place> places){
        this.places = places;
        departments = new ArrayList<>();
        /*
        * Build a list of the names of the departments to act as the "category" names
        * for the expandable cards in the RecyclerView
        * */
        for(int i=0; i<places.size(); i++){
            boolean add = true;
            for(int j=0; j<departments.size(); j++){
                if(departments.get(j).equals(places.get(i).getDepartment())) {
                    add = false;
                    break;
                }
            }
            if(add)
                departments.add(places.get(i).getDepartment());
        }
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        CardView cardView = holder.cardView; //An expandable CardView
        /*
        * Holds the list of places under a certain category. Visibility toggled between
        * VISIBLE and GONE to achieve expandability of CardView
        * */
        final LinearLayout linearLayout =
                (LinearLayout) cardView.findViewById(R.id.frag_places_item_list);

        /*
        * Build a list of the names of the actual places that will be listed under each category
        * */
        String department = departments.get(position);
        List<String> placeNames = new ArrayList<>();
        for(int i=0; i<places.size(); i++){
            if(places.get(i).getDepartment().equals(department))
                placeNames.add(places.get(i).getShortname());
        }

        /*
        * A height sufficient to display one line of text times the number of lines
        * to be displayed
        * */
        linearLayout.setMinimumHeight(50*placeNames.size());
        linearLayout.removeAllViews(); //Prevents duplication of place names
        TextView title = (TextView) cardView.findViewById(R.id.frag_places_item_category_name);
        title.setText(department);
        /*
        * Create, customize and populate TextViews to hold each place name.
        * Also set an OnClickListener to launch the MapActivity, sending an intent
        * extra or bundle
        * */
        for(int i=0; i<placeNames.size(); i++){
            final String name = placeNames.get(i);
            TextView textView = new TextView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            }
            textView.setTextSize(20);
            textView.setMinHeight(50);
            textView.setClickable(true);
            textView.setPadding(0, 0, 0, 5);
            textView.setText(name);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked " +
                            name, Toast.LENGTH_SHORT).show();
                    //TODO: Start MapActivity here with IntentExtra
                }
            });
            linearLayout.addView(textView);
        }

        final ImageButton expandableArrow =
                (ImageButton) cardView.findViewById(R.id.frag_places_item_card_arrow);
        cardView.setOnClickListener(null); //Prevents unexpected expanding/retracting of cards when scrolling
        /*
        * Expand and retract CardView by toggling visibility of it's child LinearLayout
        * and switching the image on the ImageButton
        * */
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = linearLayout.getVisibility();
                if(visibility == View.VISIBLE){
                    expandableArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
                    linearLayout.setVisibility(View.GONE);
                }
                else{
                    expandableArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_item, parent, false));
    }

    public void updatePlaces(List<Place> places){
        this.places = places;
        notifyDataSetChanged();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public PlacesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
