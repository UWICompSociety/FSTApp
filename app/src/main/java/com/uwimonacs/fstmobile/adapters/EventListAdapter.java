package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 8/9/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {



    public class EventViewHolder extends RecyclerView.ViewHolder
    {

        TextView eventNameText;
        TextView eventDescText;
        TextView eventDateText;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventNameText = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDescText = (TextView) itemView.findViewById(R.id.eventDesc);
            eventDateText = (TextView) itemView.findViewById(R.id.eventDate);
        }
    }


    Context ctxt;
    List<Event> events;

    public EventListAdapter(Context ctxt, List<Event> events)
    {
        this.ctxt = ctxt;
        this.events = new ArrayList<>(events);
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event_item,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        String title = events.get(position).getTitle();
        String desc = events.get(position).getDescription();
        String date = events.get(position).getDate();

        holder.eventNameText.setText(title);
        holder.eventDescText.setText(desc);
        holder.eventDateText.setText(date);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateEventList(List<Event> new_events)
    {
        this.events = new_events;
        notifyDataSetChanged();
    }


}
