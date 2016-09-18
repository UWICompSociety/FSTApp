package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.BusRoutesDetailActivity;
import com.uwimonacs.fstmobile.models.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sultanofcardio
 */
public class BusRoutesAdapter extends RecyclerView.Adapter<BusRoutesAdapter.BusViewHolder> {
    private List<Bus> routes;

    public BusRoutesAdapter(){
        this.routes = new ArrayList<>();
    }

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_bus_item, parent, false);
        return new BusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BusViewHolder holder, int position) {
        Bus bus = routes.get(position);

        String title = "Bus Route " + bus.getRoute().getTitle();
        holder.title.setText(title);

        String route = bus.getRoute().getName();
        holder.name.setText(route);

        String currentPosition;

        try {
            currentPosition = "Currently " + bus.getRoute().getCurrentTrip().whereIsBus();
        } catch (IndexOutOfBoundsException e){
            currentPosition = "Currently off duty";
        }
        holder.current.setText(currentPosition);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void updateList(List<Bus> routes){
        this.routes = routes;
        notifyDataSetChanged();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public TextView title, name, current;

        public BusViewHolder(final View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.bus_thumbnail);
            title = (TextView) itemView.findViewById(R.id.bus_route_title);
            name = (TextView) itemView.findViewById(R.id.bus_route_name);
            current = (TextView) itemView.findViewById(R.id.current_position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detail = new Intent();
                    detail.setClass(itemView.getContext(), BusRoutesDetailActivity.class);
                    detail.putExtra("title", title.getText());
                    itemView.getContext().startActivity(detail);
                }
            });
        }
    }
}
