package com.uwimonacs.fstmobile.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.ContactsActivity;
import com.uwimonacs.fstmobile.activities.FAQActivity;
import com.uwimonacs.fstmobile.activities.GalleryActivity;
import com.uwimonacs.fstmobile.activities.ScholarshipActivity;
import com.uwimonacs.fstmobile.activities.VideoListActivity;

public class InfoFragment extends Fragment
        implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.frag_info, container, false);

        final CardView card_faq = (CardView) v.findViewById(R.id.faq);
        final CardView card_videos = (CardView) v.findViewById(R.id.videos);
        final CardView card_scholarships = (CardView) v.findViewById(R.id.scholarships);
        final CardView card_contacts = (CardView) v.findViewById(R.id.contacts);
        final CardView card_gallery = (CardView)v.findViewById(R.id.gallery);

        /*
         * The event handler is registered at runtime instead of in the onClick XML attribute of
         * the Button nodes because the View hierarchy is in a Fragment.
         */
        card_faq.setOnClickListener(this);
        card_videos.setOnClickListener(this);
        card_scholarships.setOnClickListener(this);
        card_contacts.setOnClickListener(this);
        card_gallery.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faq:
                Pair<View, String> faqPair = Pair.create(v,"faq_card");
                ActivityOptionsCompat faqOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), faqPair);
                if(Build.VERSION.SDK_INT < 21)
                    v.getContext().startActivity(new Intent(v.getContext(), FAQActivity.class));
                else
                    v.getContext().startActivity(new Intent(v.getContext(), FAQActivity.class), faqOptions.toBundle());
                break;

            case R.id.videos:
                Pair<View, String> videosPair = Pair.create(v,"video_card");
                ActivityOptionsCompat videosOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), videosPair);
                if(Build.VERSION.SDK_INT < 21)
                    v.getContext().startActivity(new Intent(v.getContext(), VideoListActivity.class));
                else
                    v.getContext().startActivity(new Intent(v.getContext(), VideoListActivity.class), videosOptions.toBundle());
                break;

            case R.id.scholarships:
                Pair<View, String> scholPair = Pair.create(v,"scholarship_card");
                ActivityOptionsCompat scholOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), scholPair);
                if(Build.VERSION.SDK_INT < 21)
                    v.getContext().startActivity(new Intent(v.getContext(), ScholarshipActivity.class));
                else
                    v.getContext().startActivity(new Intent(v.getContext(), ScholarshipActivity.class), scholOptions.toBundle());
                break;

            case R.id.contacts:
                Pair<View, String> contactPair = Pair.create(v,"contact_card");
                ActivityOptionsCompat contactOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), contactPair);
                if(Build.VERSION.SDK_INT < 21)
                    v.getContext().startActivity(new Intent(v.getContext(), ContactsActivity.class));
                else
                    v.getContext().startActivity(new Intent(v.getContext(), ContactsActivity.class), contactOptions.toBundle());
                break;

            case R.id.gallery:
                v.getContext().startActivity(new Intent(v.getContext(),GalleryActivity.class));
                break;

            default:
                break;
        }
    }

    public InfoFragment() { /* required empty constructor */ }
}
