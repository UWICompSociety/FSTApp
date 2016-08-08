package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.ImageItem;

import java.util.ArrayList;

/**
 * Created by Matthew on 8/8/2016.
 */
public class GridViewAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private ArrayList images  = new ArrayList();

    public GridViewAdapter(Context context, int resourceId, ArrayList images) {
        super(context, resourceId, images);
        this.resourceId = resourceId;
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.grid_gallery_item,parent,false);
            holder = new ViewHolder();
            holder.image = (ImageView)row.findViewById(R.id.image);
            holder.imageTitle = (TextView)row.findViewById(R.id.text);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        ImageItem imageItem = (ImageItem)images.get(position);
        holder.imageTitle.setText(imageItem.getTitle());
        Picasso.with(context).load(imageItem.getUrl()).into(holder.image);
        return row;

    }






    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
