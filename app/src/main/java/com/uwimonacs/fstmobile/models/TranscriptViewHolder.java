package com.uwimonacs.fstmobile.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

public class TranscriptViewHolder extends RecyclerView.ViewHolder {
    public TextView termName;
    public LinearLayout linearLayout;
    public TranscriptViewHolder(View itemView) {
        super(itemView);
        termName = (TextView) itemView.findViewById(R.id.term_name);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.term_card_linearlayout);
    }
}
