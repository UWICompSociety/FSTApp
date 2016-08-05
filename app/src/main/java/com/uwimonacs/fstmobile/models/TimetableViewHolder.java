package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class TimetableViewHolder extends RecyclerView.ViewHolder {
    public final TextView codeTitle, times, venue;
    public final LinearLayout classCard;

    public TimetableViewHolder(View v) {
        super(v);

        classCard = (LinearLayout) v;
        codeTitle = (TextView) v.findViewById(R.id.timetable_course_code_title);
        times = (TextView) v.findViewById(R.id.timetable_times);
        venue = (TextView) v.findViewById(R.id.timetable_venue);
    }
}
