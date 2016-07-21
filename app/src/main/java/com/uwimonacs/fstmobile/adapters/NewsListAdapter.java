package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.NewsDetailActivity;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/11/2016.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {



    public class NewsViewHolder extends RecyclerView.ViewHolder
    {
        TextView newsTitle;
        TextView newsDesc;
        ImageView newsImage;
        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView)itemView.findViewById(R.id.newsHeading);
            newsDesc = (TextView)itemView.findViewById(R.id.newsDescription);
            newsImage = (ImageView)itemView.findViewById(R.id.newsImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = NewsViewHolder.this.getAdapterPosition(); //position of element in list

                    Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                    intent.putExtra("image",newsList.get(pos).getImage_url());
                    intent.putExtra("title",newsList.get(pos).getTitle());
                    intent.putExtra("story",newsList.get(pos).getStory());

                    v.getContext().startActivity(intent);


                }
            });
        }
    }

    List<News> newsList;
    Context ctxt;
    public NewsListAdapter(Context ctxt, List<News> newsList)
    {
        this.ctxt = ctxt;
        this.newsList = new ArrayList<>(newsList);

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item,parent,false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
       // holder.newsImage.setImageResource(newsList.get(position).getImage());
        Picasso.with(ctxt).load(newsList.get(position).getImage_url()).into(holder.newsImage);
        holder.newsDesc.setText(newsList.get(position).getDescription());
        holder.newsTitle.setText(newsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateNews(List<News> new_news)
    {
        this.newsList = new ArrayList<>(new_news);
        notifyDataSetChanged();
    }
}
