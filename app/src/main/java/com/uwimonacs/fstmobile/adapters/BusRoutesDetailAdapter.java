package com.uwimonacs.fstmobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.AsyncResponse;
import com.uwimonacs.fstmobile.models.Bus;
import com.uwimonacs.fstmobile.models.Bus.*;
import com.uwimonacs.fstmobile.models.BusTask;
import com.uwimonacs.fstmobile.models.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author sultanofcardio
 */
public class BusRoutesDetailAdapter extends RecyclerView.Adapter<BusRoutesDetailAdapter.BusViewHolderDetail>
implements AsyncResponse{
    private List<String> times, stopNames;
    private List<Stop> stops;
    private Context context;
    private String title;
    private int duration;

    public BusRoutesDetailAdapter(Context context, String title){
        this.times = new ArrayList<>();
        this.stopNames = new ArrayList<>();
        this.stops = new ArrayList<>();
        this.context = context;
        this.title = title;
        new BusTask(this, context).execute();
    }

    @Override
    public BusViewHolderDetail onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_bus_item_detail, parent, false);
        return new BusViewHolderDetail(v);
    }

    @Override
    public void onBindViewHolder(BusViewHolderDetail holder, int position) {
        Stop stop = stops.get(position);

        int inithour = stop.getTime().get(Calendar.HOUR_OF_DAY);
        int minute = stop.getTime().get(Calendar.MINUTE);

        String time = formatString(inithour, minute);
        String location = stop.getName();

        holder.time.setText(time);
        holder.location.setText(location);
    }

    private String formatString(int inithour, int minute){
        int hour;
        if(inithour > 12)
            hour = inithour - 12;
        else
            hour = inithour;

        String time = "" + hour
                + ":" + minute;

        if(minute == 0)
            time += "0";
        else if(minute < 10 && minute > 0)
            time = time.substring(0, time.length()-1) + "0" + time.substring(time.length()-1);

        if(inithour >= 12)
            time += " PM";
        else
            time += " AM";

        return time;
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    @Override
    public void processFinish(ArrayList<Bus> buses) {
        Bus bus = new Bus();
        switch(title){
            case "A":
                bus = buses.get(0);
                break;
            case "B":
                bus = buses.get(1);
                break;
            case "C":
                bus = buses.get(2);
                break;
            case "D":
                bus = buses.get(3);
                break;
        }

        Trip trip;
        try {
            trip = bus.getRoute().getCurrentTrip();
        } catch(IndexOutOfBoundsException e){
            trip = bus.getRoute().getTrips().get(0);
        }
        this.stops = trip.getStops();
        notifyDataSetChanged();
        determineDuration();
    }

    private void determineDuration(){
        switch(title){
            case "A":
                duration = 1;
                break;
            case "B":
                duration = 1;
                break;
            case "C":
                duration = 2;
                break;
            case "D":
                duration = 2;
                break;
        }
    }

    public class BusViewHolderDetail extends RecyclerView.ViewHolder{
        public TextView time, location;

        public BusViewHolderDetail(final View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.route_time);
            location = (TextView) itemView.findViewById(R.id.time_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = location.getText().toString();
                    String detail = "Bus passes here every ";

                    if(duration == 1)
                        detail += "hour";
                    else
                        detail += "half hour";

                    final AlertDialog dialog = new AlertDialog.Builder(itemView.getContext()).create();
                    final LayoutInflater inflater = LayoutInflater.from(itemView.getContext());

                    @SuppressLint("InflateParams")
                    // Pass null as the parent view because its going in the dialog layout
                    final View dialogView = inflater.inflate(R.layout.dialog_content_bus_stop, null);

                    dialog.setTitle(name);

                    final TextView stopDetail = (TextView) dialogView.findViewById(R.id.stop_detail);
                    stopDetail.setText(detail);

                    dialog.setView(dialogView);
                    dialog.show();
                }
            });
        }
    }
}
