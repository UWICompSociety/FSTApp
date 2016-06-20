package com.uwimonacs.fstmobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwimonacs.fstmobile.R;

/**
 * Created by Matthew on 6/20/2016.
 */
public class InfoFragment extends Fragment {


    private View view;


    public InfoFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_info,container,false); //inflates the layout for the view

        return view;
    }
}
