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

/**
 * Created by Matthew on 6/20/2016.
 */
public class InfoFragment extends Fragment {


    private View view;
    private RecyclerView infoRecView;
    private InfoListAdapter infoListAdapter;
    String[] titles = {"FAQ","Videos","Scholarships & Busaries"}; //list of titles for info  fragment
    int[] images = {R.drawable.faqimage,R.drawable.video,R.drawable.scholarship}; //list of images for info fragment


    public InfoFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_info,container,false); //inflates the layout for the view

        infoRecView = (RecyclerView)view.findViewById(R.id.listInfo); //referencing recycler view
        infoListAdapter = new InfoListAdapter(getActivity(),titles,images);  //creating the list adapter

        LinearLayoutManager llm = new LinearLayoutManager(getActivity()); //creating layout manager necessary for recycler view to function

        infoRecView.setLayoutManager(llm);
        infoRecView.setHasFixedSize(true);

        infoRecView.setAdapter(infoListAdapter); //setting the adapter of the list


        return view;
    }
}
