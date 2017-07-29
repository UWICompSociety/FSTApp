package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.ImageShackAlbum;

import java.util.List;

/**
 * Created by Akinyele on 7/28/2017.
 */

public class ImageShackAlbumAdapter extends RecyclerView.Adapter<ImageShackAlbumAdapter.AlbumViewHolder> {

    private final Context context;
    private ImageShackAlbum imageShackAlbum;
    private List<ImageShackAlbum.ResultBean.ImagesBean> images;

    public ImageShackAlbumAdapter(Context context, ImageShackAlbum imageShackAlbum){

        this.context = context;
        this.imageShackAlbum = imageShackAlbum;
        images = imageShackAlbum.getResult().getImages();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bottmsheet_images,parent,false);

        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {

        Picasso.with(context).load(images.get(position).getDirect_link()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public AlbumViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.bottom_sheet_image);
        }
    }

}
