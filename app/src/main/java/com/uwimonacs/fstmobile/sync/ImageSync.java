package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.models.ImageItem;
import com.uwimonacs.fstmobile.rest.RestEvent;
import com.uwimonacs.fstmobile.rest.RestImage;

import java.util.ArrayList;

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
}
