package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.NewsDetailActivity;
import com.uwimonacs.fstmobile.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/11/2016.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    private List<News> newsList;
    private final Context ctxt;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        final TextView newsTitle;
        final TextView newsDesc;
        final ImageView newsImage;

        public NewsViewHolder(View v) {
            super(v);

            newsTitle = (TextView) v.findViewById(R.id.newsHeading);
            newsDesc = (TextView) v.findViewById(R.id.newsDescription);
            newsImage = (ImageView) v.findViewById(R.id.newsImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = NewsViewHolder.this.getAdapterPosition();

                    final Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                    intent.putExtra("image", newsList.get(pos).getImage_url());
                    intent.putExtra("title", newsList.get(pos).getTitle());
                    intent.putExtra("story", newsList.get(pos).getStory());
                    intent.putExtra("url", newsList.get(pos).getUrl());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public NewsListAdapter(Context ctxt, List<News> newsList) {
        this.ctxt = ctxt;
        this.newsList = new ArrayList<>(newsList);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_news_item, parent, false);

        return new NewsViewHolder(v);
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

    public void updateNews(List<News> new_news) {
        this.newsList = new ArrayList<>(new_news);
        notifyDataSetChanged();
    }


}
