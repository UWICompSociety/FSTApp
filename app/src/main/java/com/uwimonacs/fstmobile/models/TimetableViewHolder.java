package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class TimetableViewHolder extends RecyclerView.ViewHolder {
    public TextView codeTitle, times, venue;
    public LinearLayout classCard;
    public TimetableViewHolder(View itemView) {
        super(itemView);
        classCard = (LinearLayout) itemView;
        codeTitle = (TextView) itemView.findViewById(R.id.timetable_course_code_title);
        times = (TextView) itemView.findViewById(R.id.timetable_times);
        venue = (TextView) itemView.findViewById(R.id.timetable_venue);
    }
}
