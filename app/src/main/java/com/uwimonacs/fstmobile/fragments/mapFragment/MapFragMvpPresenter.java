package com.uwimonacs.fstmobile.fragments.mapFragment;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.uwimonacs.fstmobile.models.locations.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Kyzer on 5/24/2017.
 */

public interface MapFragMvpPresenter {

    /**
     *
     * @return
     */
    ArrayList<String> getRoomsFromDb();

    /**
     *
     * @return
     */
    Collection<? extends String> getBuildingsFromDb();

    /**
     *
     * @return
     */
    boolean getPath();

    /**
     *
     * @param checked
     * @return
     */
    boolean toggleLocation(boolean checked);

    /**
     *
     * @param infoImage
     * @param filepath
     */
    void setPic(ImageView infoImage, String filepath);


    /**
     *
     * @return
     */
    HashMap<String,Vertex> getNodes();

    /**
     *
     * @param ll
     */
    void setSource(LatLng ll, int level);

    /**
     * Method used to removed path and show the initial path finding button
     */
    void cancelPathFinding();

    /**
     * Method used to removed path and show the inital path finding button
     * and increases the familiarity of the destination.
     */
    void destinationArrived();


    /**
     * Used to gather the information about a specific place
     * @param id
     */
    String getPlaceInfo(String id);
}
