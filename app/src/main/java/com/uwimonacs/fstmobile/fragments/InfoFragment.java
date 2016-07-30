package com.uwimonacs.fstmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.ContactsActivity;
import com.uwimonacs.fstmobile.activities.FAQActivity;
import com.uwimonacs.fstmobile.activities.ScholarshipActivity;
import com.uwimonacs.fstmobile.activities.VideoListActivity;
import com.uwimonacs.fstmobile.helper.Connect;

public class InfoFragment extends Fragment
        implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_info, container, false);

        CardView card_faq = (CardView) v.findViewById(R.id.faq);
        CardView card_videos = (CardView) v.findViewById(R.id.videos);
        CardView card_scholarships = (CardView) v.findViewById(R.id.scholarships);
        CardView card_contacts = (CardView) v.findViewById(R.id.contacts);

        card_faq.setOnClickListener(this);
        card_videos.setOnClickListener(this);
        card_scholarships.setOnClickListener(this);
        card_contacts.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faq:
                Intent faqIntent = new Intent(v.getContext(), FAQActivity.class);
                v.getContext().startActivity(faqIntent);
                break;

            case R.id.videos:
               // if (new Connect(v.getContext()).isConnected()) {
                    Intent videoIntent = new Intent(v.getContext(), VideoListActivity.class);
                    v.getContext().startActivity(videoIntent);
               // } else {
               //     Snackbar.make(v, "Check your Internet connection", Snackbar.LENGTH_SHORT).show();
               // }
                break;

            case R.id.scholarships:
                Intent scholarshipIntent = new Intent(v.getContext(), ScholarshipActivity.class);
                v.getContext().startActivity(scholarshipIntent);
                break;

            case R.id.contacts:
                Intent contactIntent = new Intent(v.getContext(), ContactsActivity.class);
                v.getContext().startActivity(contactIntent);
                break;

            default:
                break;
        }
    }

    public InfoFragment() { /* required empty constructor */ }
}
