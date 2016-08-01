package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by sultanofcardio on 7/27/16.
 */
public class TermsAdapter extends ArrayAdapter<String> {
    public TermsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void update(List<String> strings) {
        this.addAll(strings);
        notifyDataSetChanged();
    }
}