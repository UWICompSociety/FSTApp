package com.uwimonacs.fstmobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.InfoListAdapter;

@SuppressWarnings("FieldCanBeLocal")
public class InfoFragment extends Fragment {
    private View view;
    private RecyclerView infoRecView;
    private InfoListAdapter infoListAdapter;

    String[] titles = {"FAQ", "Videos", "Scholarships & Bursaries", "Contacts"};
    int[] images = {R.drawable.ic_info_black_24dp, R.drawable.ic_videocam_black_24dp,
            R.drawable.ic_school_black_24dp, R.drawable.ic_contacts_black_24dp};

    public InfoFragment()
    {
        /* required empty constructor */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_info, container, false);

        infoRecView = (RecyclerView)view.findViewById(R.id.listInfo);
        infoListAdapter = new InfoListAdapter(getActivity(), titles, images);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        infoRecView.setLayoutManager(llm);
        infoRecView.setHasFixedSize(true);

        infoRecView.setAdapter(infoListAdapter);

        return view;
    }
}
