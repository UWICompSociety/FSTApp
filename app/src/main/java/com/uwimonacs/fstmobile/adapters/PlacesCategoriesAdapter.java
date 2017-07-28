package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PermissionsActivity;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesCategoriesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<Place> places;
    private List<String> departments;
    private RecyclerView rv;

    public PlacesCategoriesAdapter(Context context, List<Place> places, RecyclerView rv) {
        this.places = new ArrayList<>(places);
        departments = new ArrayList<>();
        this.rv = rv;
        /*
        * Build a list of the names of the departments to act as the "category" names
        * for the expandable cards in the RecyclerView
        * */
        setUpDepartments();

        this.context = context;
    }

    private void setUpDepartments() {
        for (int i = 0; i < places.size(); i++) {
            boolean add = true;
            for (int j = 0; j < departments.size(); j++) {
                if (departments.get(j).equals(places.get(i).getDepartment())) {
                    add = false;
                    break;
                }
            }
            if (add)
                departments.add(places.get(i).getDepartment());
        }
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        final CardView cardView = (CardView) vh.itemView; // an expandable CardView
        final int pos = vh.getAdapterPosition();

        /*
        * Holds the list of places under a certain category. Visibility toggled between
        * VISIBLE and GONE to achieve expandability of CardView
        * */
        final LinearLayout linearLayout =
                (LinearLayout) cardView.findViewById(R.id.frag_places_item_list);

        /*
        * Build a list of the names of the actual places that will be listed under each category
        * */
        final String department = departments.get(position);
        final List<String> placeNames = new ArrayList<>();
        final List<Place> tempPlaces = new ArrayList<>();

        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).getDepartment().equals(department) && !places.get(i).getShortname().contains("Building")) {
                placeNames.add(places.get(i).getShortname());
                tempPlaces.add(places.get(i));
            }
        }

        /*
        * A height sufficient to display one line of text times the number of lines
        * to be displayed
        * */
        linearLayout.setMinimumHeight(50 * placeNames.size());
        linearLayout.removeAllViews(); //Prevents duplication of place names
        final TextView title = (TextView) cardView.findViewById(R.id.frag_places_item_category_name);
        title.setText(department);
        /*
        * Create, customize and populate TextViews to hold each place name.
        * Also set an OnClickListener to launch the MapActivity, sending an intent
        * extra or bundle
        * */
        for (int i = 0; i < placeNames.size(); i++) {
            final String name = placeNames.get(i);
            final Place place = tempPlaces.get(i);
            final TextView textView = new TextView(context);

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
                public void onClick(View v) {
                    Intent mapIntent = new Intent(v.getContext(), PermissionsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("places", places);
                    mapIntent.putExtra("location", place.getLocation());
                    mapIntent.putExtra("department",place.getDepartment());
                    mapIntent.putExtra("shortname",name);
                    mapIntent.putExtra("fullname",place.getFullname());
                    mapIntent.putExtra("placesList", bundle);
                    v.getContext().startActivity(mapIntent);
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
            public void onClick(View v) {
                int visibility = linearLayout.getVisibility();
                if (visibility == View.VISIBLE){
                    expandableArrow.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    linearLayout.setVisibility(View.GONE);
                }
                else{
                    expandableArrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    linearLayout.setVisibility(View.VISIBLE);
                    /*
                    * Causes RecyclerView to scroll to the bottom if at the last element
                    * to reveal pseudo-hidden content
                    * */
                    if(pos == departments.size() - 1){
                        System.out.println("You clicked me");
                        ((FragmentActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rv.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.scrollToPosition(departments.size() - 1);
                                    }
                                }, 50);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_item, parent, false)) {
        };
    }

    public void updatePlaces(List<Place> places){
        this.places = new ArrayList<>(places);
        departments = new ArrayList<>();
        setUpDepartments();
        notifyDataSetChanged();
    }
}
