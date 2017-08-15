package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PlacesGallerySliderActivity;
import com.uwimonacs.fstmobile.models.ImageShackAlbum;
import com.uwimonacs.fstmobile.models.ImagesShackAlbumList;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Akinyele on 7/28/2017.
 *
 * Gets the images from ImageshackApi and display the
 */


public class ImageShackAlbumAdapter extends RecyclerView.Adapter<ImageShackAlbumAdapter.AlbumViewHolder> {


    private final Context context;
    private ImagesShackAlbumList.ResultType.AlbumsType imageShackAlbum;
    private List<ImagesShackAlbumList.ResultType.AlbumsType.ImagesType> images;

    public ImageShackAlbumAdapter(Context context, ImagesShackAlbumList.ResultType.AlbumsType imageShackAlbum){

        this.context = context;
        this.imageShackAlbum = imageShackAlbum;
        images = imageShackAlbum.getImages();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bottmsheet_images,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, final int position) {

        String Url = "http://"+(images.get(position).getDirect_link());
        Picasso.with(context)
                .setLoggingEnabled(true);
        Picasso.with(context)
                .load(Url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_no_image_found)
//                .resize(20,20)
//                .centerCrop()
                .fit()
                .centerInside()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("onSuccess: ", "sucsess" );
                    }

                    @Override
                    public void onError() {
                        Log.d("onError: ", "error ");
                    }
                });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"clicked", Toast.LENGTH_LONG);
                Log.d("onClick: ", images.get(position).getFilename()+ " clicked");
                Intent intent = new Intent(context,PlacesGallerySliderActivity.class );

                ArrayList<String> urls = new ArrayList<String>();
                for (ImagesShackAlbumList.ResultType.AlbumsType.ImagesType img: images
                     ) {
                    urls.add(img.getDirect_link());
                }
                intent.putExtra("URLs", urls);
                intent.putExtra("Position", position);
                context.startActivity(intent);
            }


        });
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
