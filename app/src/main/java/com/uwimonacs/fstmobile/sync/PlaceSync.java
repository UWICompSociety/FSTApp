package com.uwimonacs.fstmobile.sync;
import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.Place;
import com.uwimonacs.fstmobile.rest.RestPlace;

import java.util.ArrayList;

/**
 * Created by Matthew on 7/19/2016.
 */
public class PlaceSync {
    private final String url;
    private ArrayList<Place> places;

    public PlaceSync(String url)
    {
        this.url = url;
    }

    public boolean syncPlaces() {
        final RestPlace restPlace = new RestPlace(url);

        places = restPlace.getPlaces(); // gets the list of places from rest api

        if (places == null) // if there are no places
            return false;

        if (places.size() == 0) // if the place list is empty
            return false;

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < places.size(); i++) {
                Place place = places.get(i);

                Place.findOrCreateFromJson(place); // saves places to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }
}
