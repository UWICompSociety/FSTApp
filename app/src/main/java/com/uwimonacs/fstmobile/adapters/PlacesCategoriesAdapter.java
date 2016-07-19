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
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Room;

import java.util.List;

public class PlacesCategoriesAdapter extends RecyclerView.Adapter<PlacesCategoriesAdapter.PlacesViewHolder> {
    private Context context;
    private List<Room> rooms;

    public PlacesCategoriesAdapter(Context context){
        rooms = Constants.ROOMS;
        this.context = context;
    }

    @Override
    public int getItemCount() {

        return Constants.PLACE_CATEGORIES.length;
//        return searchResults.size();
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        final LinearLayout linearLayout = (LinearLayout) cardView.findViewById(R.id.frag_places_item_list);
        linearLayout.setMinimumHeight(50*Constants.SUB_CATEGORIES.length);
        linearLayout.removeAllViews();
        for(int i=0; i<Constants.SUB_CATEGORIES.length; i++){
            final String subCategory = Constants.SUB_CATEGORIES[i];
            TextView textView = new TextView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            }
            textView.setTextSize(20);
            textView.setMinHeight(50);
            textView.setClickable(true);
            textView.setPadding(0, 0, 0, 5);
            textView.setText(subCategory);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked " +
                            subCategory, Toast.LENGTH_SHORT).show();
                    //TODO: Start MapActivity here
                }
            });
            linearLayout.addView(textView);
        }
        final ImageButton expandableArrow =
                (ImageButton) cardView.findViewById(R.id.frag_places_item_card_arrow);
        cardView.setOnClickListener(null);
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
        TextView title = (TextView) cardView.findViewById(R.id.frag_places_item_category_name);
        title.setText(Constants.PLACE_CATEGORIES[position]);
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_places_item, parent, false));
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public PlacesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
