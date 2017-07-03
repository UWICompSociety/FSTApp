package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

/**
 * Created by jourdan on 7/2/17.
 */

public class IconAdapter extends BaseAdapter {
    private Context mContext;
    private String[] titles;
    private int[] images;

    public IconAdapter(Context c,String[] web,int[] Imageid ) {
        mContext = c;
        this.images = Imageid;
        this.titles = web;
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_cell, null);
            TextView textView = (TextView)grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(titles[i]);
            imageView.setImageResource(images[i]);
        }else{
            grid = (View)view;
        }
        return grid;
    }
}
