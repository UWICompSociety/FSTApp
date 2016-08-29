package com.uwimonacs.fstmobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthew on 8/28/2016.
 */
public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {


    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle;
        TextView tvTime;
        TextView tvVenue;
        public ScheduleViewHolder(View v) {
            super(v);
            tvTime = (TextView) v.findViewById(R.id.time);
            tvTitle = (TextView) v.findViewById(R.id.title);
            tvVenue = (TextView) v.findViewById(R.id.venue);
        }
    }

    List<Schedule> scheduleList;

    public ScheduleListAdapter(List<Schedule> scheduleList)
    {
        this.scheduleList = new ArrayList<>(scheduleList);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schedule_item,parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        holder.tvTitle.setText(scheduleList.get(position).getTitle());
        holder.tvTime.setText(scheduleList.get(position).getTime());
        holder.tvVenue.setText(scheduleList.get(position).getVenue());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void updateSchedules(List<Schedule> schedules)
    {
        this.scheduleList = new ArrayList<>(schedules);
        notifyDataSetChanged();
    }
}
