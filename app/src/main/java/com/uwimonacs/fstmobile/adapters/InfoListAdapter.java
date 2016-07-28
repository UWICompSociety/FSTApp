package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.ContactsActivity;
import com.uwimonacs.fstmobile.activities.FAQActivity;
import com.uwimonacs.fstmobile.activities.ScholarshipActivity;
import com.uwimonacs.fstmobile.activities.VideoListActivity;
import com.uwimonacs.fstmobile.helper.Connect;

/**
 * Created by Matthew on 6/25/2016.
 */
public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {


    public static class InfoViewHolder extends RecyclerView.ViewHolder
    {
        TextView infoTitle; //reference to the title text
        ImageView infoImage; //reference to the info image

        public InfoViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = InfoViewHolder.this.getAdapterPosition(); //position of element in list
                    switch(pos)
                    {
                        case 0:
                            Intent faqIntent = new Intent(v.getContext(),FAQActivity.class);
                            v.getContext().startActivity(faqIntent); //starting FAQ activity
                            break;
                        case 1:
                            if(new Connect(v.getContext()).isConnected()) {
                                Intent videoIntent = new Intent(v.getContext(), VideoListActivity.class);
                                v.getContext().startActivity(videoIntent); //starting video activity
                            } else {
                                Toast.makeText(v.getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            //Scholarship Actvity
                            Intent scholarshipIntent = new Intent(v.getContext(),ScholarshipActivity.class);
                            v.getContext().startActivity(scholarshipIntent);
                            break;
                        case 3:
                            //Contact Actvity
                            Intent contactIntent = new Intent(v.getContext(),ContactsActivity.class);
                            v.getContext().startActivity(contactIntent);

                    }
                }
            });

            infoTitle = (TextView)itemView.findViewById(R.id.info_title);
            infoImage = (ImageView) itemView.findViewById(R.id.image_info);
        }
    }

    String[] titles;
    int[] images;
    Context ctxt;

    public InfoListAdapter(Context ctxt,String[] titles,int[] images)
    {
        this.ctxt = ctxt;
        this.titles = titles;
        this.images = images;

    }


    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_info_item,parent,false); //inflate view with layout file
        final InfoViewHolder infoViewHolder = new InfoViewHolder(view);



        return infoViewHolder;
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {

        //setting values
        holder.infoTitle.setText(titles[position]);
        holder.infoImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length; //number of elements in list
    }
}
