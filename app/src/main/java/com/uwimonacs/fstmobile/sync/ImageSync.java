package com.uwimonacs.fstmobile.sync;

import android.media.Image;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.models.ImageItem;
import com.uwimonacs.fstmobile.rest.RestEvent;
import com.uwimonacs.fstmobile.rest.RestImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 8/8/2016.
 */
public class ImageSync {

    private final String url;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();

    public ImageSync(String url)
    {
        this.url = url;
    }

    public boolean syncImages() {

        final RestImage restImage = new RestImage(url);

        imageItems = restImage.getImages(); // gets the list of images from rest api

        if (imageItems == null) // if there are no contacts
            return false;

        if (imageItems.size() == 0) // if the images list is empty
            return false;

        ActiveAndroid.beginTransaction();

        try {
            deleteStaleData();
            for (int i = 0; i < imageItems.size(); i++) {
                final ImageItem imageItem = imageItems.get(i);

                ImageItem.findOrCreateFromJson(imageItem); // saves image to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }

    private void deleteStaleData()
    {

        List<ImageItem> stale_images = new Select().all().from(ImageItem.class).execute();
        for(int i=0;i<stale_images.size();i++)
        {
            if(!doesImageExistInJson(stale_images.get(i)))
            {
                ImageItem.delete(ImageItem.class,stale_images.get(i).getId());
            }
        }
    }

    private boolean doesImageExistInJson(ImageItem items)
    {
        return imageItems.contains(items);
    }
}
