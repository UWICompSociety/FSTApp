package com.uwimonacs.fstmobile.models;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matthew on 8/8/2016.
 */

@Table(name="ImageItem")
public class ImageItem extends Model {

    @SerializedName("id")
    @Column(name="imageId")
    private int imageId;

    @SerializedName("name")
    @Column(name="title")
    private String title;

    @SerializedName("url")
    @Column(name="url")
    private String url;



    public ImageItem()
    {
        super();
    }
    public ImageItem(String url,String title)
    {
        super();
        this.url = url;
        this.title =title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static ImageItem findOrCreateFromJson(ImageItem new_image) {
        int imageId = new_image.getImageId();
        ImageItem existingImage =
                new Select().from(ImageItem.class).where("imageId = ?", imageId).executeSingle();
        if (existingImage != null) {
            // found and return existing
            UpdateImage(existingImage,new_image);
            return existingImage;
        } else {
            // create and return new user
            ImageItem imageItem = new_image;
            imageItem.save();
            return imageItem;
        }
    }

    private static void UpdateImage(ImageItem old_image,ImageItem new_image)
    {
        old_image.setTitle(new_image.getTitle());
        old_image.setUrl(new_image.getUrl());
        old_image.save();

    }
}
