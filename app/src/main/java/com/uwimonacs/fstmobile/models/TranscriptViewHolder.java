package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class TranscriptViewHolder extends RecyclerView.ViewHolder {
    public TextView termName, termGPA;
    public LinearLayout linearLayout;
    public TranscriptViewHolder(View itemView) {
        super(itemView);
        termName = (TextView) itemView.findViewById(R.id.term_name);
        termGPA = (TextView) itemView.findViewById(R.id.term_gpa);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.term_card_linearlayout);
    }
}
