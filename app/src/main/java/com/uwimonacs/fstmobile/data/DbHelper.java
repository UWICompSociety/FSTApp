package com.uwimonacs.fstmobile.data;

import android.database.Cursor;

import com.uwimonacs.fstmobile.models.locations.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyzer on 5/24/2017.
 */

public interface DbHelper {

    Cursor findLocation(String location);

    Cursor findLocation(double lat, double lng, String id);

    Cursor getVertices();

    Cursor getRooms();

    Cursor getBuilding();

    List<Place> getLocations();

    ArrayList<String> roomList();

    ArrayList<String> buildingList();

    Cursor getEdges();

    void updateRoom(String id, Integer known, double familiarity);

    void insertLandmark(double lat, double lng, String name, String desc, String image_id);

}
