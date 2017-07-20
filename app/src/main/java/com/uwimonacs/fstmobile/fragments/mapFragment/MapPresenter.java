package com.uwimonacs.fstmobile.fragments.mapFragment;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.uwimonacs.fstmobile.data.AppDbHelper;
import com.uwimonacs.fstmobile.data.DbHelper;
import com.uwimonacs.fstmobile.models.locations.Place;
import com.uwimonacs.fstmobile.models.locations.Room;
import com.uwimonacs.fstmobile.models.locations.Vertex;
import com.uwimonacs.fstmobile.services.Distance;
import com.uwimonacs.fstmobile.services.Learner;
import com.uwimonacs.fstmobile.services.MapMarker;
import com.uwimonacs.fstmobile.services.MapPolylines;
import com.uwimonacs.fstmobile.services.MyLocationService;
import com.uwimonacs.fstmobile.util.Path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Kyzer on 5/24/2017.
 */

public class MapPresenter implements MapFragMvpPresenter, MyLocationService.myLocationCallback {
    private static final String TAG = "Presenter TAG: ";
    final LatLng sci_tech = new LatLng(18.005072, -76.749544);

    private MapFragMvPView view;

    //Services
    public static DbHelper dbHelper;
    private final MyLocationService location;
    private Learner learner;

    //    private final Tracker mapTracker;

    //Models
    public static Vertex start,destination;
    public static Path path;


    private static MapMarker mapMarkers;
    private MapPolylines mapPolylines;


    //Flags
    private boolean isSourceSet;
    private boolean onLocationChangedUpdateCamera = true;
    public static boolean location_service_enabled;

    //TODO remove
    private GoogleMap mGoogleMap;


    public MapPresenter(MapFragMvPView view, Activity activity, GoogleMap mGoogleMap) {
        this.view = view;
        this.dbHelper = AppDbHelper.getInstance(activity);
        this.path = Path.getInstance(dbHelper);
        this.location = MyLocationService.getInstance(activity);
        location.setOnLocationChangedListner(this);
        this.mapMarkers = MapMarker.getInstance(mGoogleMap);
        this.mapPolylines = MapPolylines.getInstance(mGoogleMap);
        this.mGoogleMap = mGoogleMap;
        this.learner = new Learner(activity);

//        this.mapTracker = new Tracker(null);
//        mapTracker.start();
    }

    /***
     * This is the Method used to look up classes on the Campus by utilizing the search bar provided.
     * It will get search through the database of class rooms and and create a destination object
     * @throws IOException
     */
    public boolean geoLocate() throws IOException {
        String cls = view.getDestText();//get the destination text from the text field
        Cursor res = dbHelper.findLocation(cls); // the location/s that matches the search
        return setVertex(res, 2);
    }


    /***
     * This Method Performs the Dijkstras algorithm on the graph for the current source and destination selected.
     */
    @Override
    public boolean getPath() {
//         location_service_enabled = ((ToggleButton) getView().findViewById(R.id.locationToggle)).isChecked(); //Finds out if user want to use their their location.
           //handles the start location configuration
        if (location_service_enabled && !isSourceSet) {
            //TODO Check location accuracy before using the point;
            LatLng myLL;
            Location my_location = location.getLocation();
            if(my_location!=null){
                myLL = new LatLng (my_location.getLatitude(), my_location.getLongitude());
            }else{
                myLL = new LatLng(my_location.getLatitude(),my_location.getLongitude());
            }

            //TODO get the floor level of the user based of the user's altitude


            if(location.isAccurate()){

            }
                int myLevel = 0;
                start = Distance.find_closest_marker(myLL, myLevel, path.getCNodes());
                isSourceSet = true;
        }else if(isSourceSet){

        }else{
            isSourceSet = setSource();
        }

        if (!isSourceSet) {
            // ask them to select a starting point
            view.makeToast("NO Source");
            return false;
        } else if (destination == null) {
            view.makeToast("No Destination selected");
            return false;
        } else {
            try {
                path.getPath(start.getId(), destination.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(path.getCurrPath()==null){
                view.makeToast("Could not find path from "+ start.getName()+ " to " + destination.getName() + " found.");
                return false;
            }else {
                view.makeToast("Path to " + destination.getName() + " found.");

            }
        }

        mapPolylines.createPath(path.getCurrPath());
        getPOI();

        return true;
    }

    /**
     * This methods should search through the vertex table
     * and find a the starting point based of where the user
     * selected in the text field.
     * It utilizes the text filed @getSource to search through the database.
     * @return
     */
    public boolean setSource() {
        if(location_service_enabled){return false;}

//        String startText = getSourceView.getText().toString();
        String startText = view.getStartText();
        Cursor res = dbHelper.findLocation(startText);
        return setVertex(res, 1);
    }

    /**
     * Method used to create the location object.
     * @param data the location/s that will be created
     * @param type the type of marker(source/destionation)
     * @return
     */
    public boolean setVertex(Cursor data, int type) {
        //TODO change method to just take the id and type;
        if (data.getCount() == 0) {
            // show message "no results found in places database"
            view.makeToast("Could not find ");
            return false;
        } else if (data.getCount() > 1) {
            //TODO more than one possible starting point found found. Create method to let them choose

            view.makeToast("Please select one these Rooms to start from.");
            return false;
        } else {
            switch (type) {
                case 1:
                    // create the starting node
                    start = path.getVertices().get(data.getString(0));
                    mapMarkers.addMarker(start, type);
                    view.goToLocation(start.getLL());
                    break;
                case 2:
                    destination = path.getVertices().get(data.getString(0)); //takes the result and uses the id to find the location
                    mapMarkers.addMarker(destination, type);

                    view.makeToast(destination.getName() + " is here");
                    view.goToLocation(destination.getLL());
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void getPOI(){
        LinkedList<Vertex> currPath = path.getCurrPath();
        ArrayList<Vertex> POI = getPointsOfInterest();
        for(int i = 0; i<currPath.size()-1; i++){
            LatLng midPoint  = SphericalUtil.interpolate(currPath.get(i).getLL(),currPath.get(i).getLL(), 0.5 );
            for( int n = 0 ; n< POI.size() ; n++){
                if( SphericalUtil.computeDistanceBetween(midPoint,POI.get(n).getLL()) < 15.0){
                    //closePoints.add(POI.remove(n));
                    Log.d(TAG, "getPOI: distance midPoint-POI.get(n).getLL() " +  SphericalUtil.computeDistanceBetween(midPoint,POI.get(n).getLL()));
                    mapMarkers.addMarker( POI.remove(n), 3);
                }
            }
        }
    }

    private ArrayList<Vertex> getPointsOfInterest() {
        ArrayList<Vertex> nodes = new ArrayList<>();
        HashMap<String,Vertex> all_nodes = path.getVertices();
        Boolean known;

        Iterator iter = all_nodes.keySet().iterator();

        while (iter.hasNext()){
            String key = (String) iter.next();
            Vertex v = all_nodes.get(key);
            if( v instanceof Place){
                known = ((Place) v).isKnown();
            }else if( v.isLandmark() ){
                known =true;
            }else if(v instanceof Room){
                known = ((Room) v).isKnown();
            } else {
                known = false;
            }
            if(known){
                nodes.add(v);
            }
        }
        return nodes;
    }

    @Override
    public boolean toggleLocation(boolean checked) {
        location_service_enabled = !location_service_enabled;

        if (checked) {
            if ( location.isGPSEnabled() ) {
                //provide some method that Uses user Location as starting point.
                view.locationEnabled(true);
                view.makeToast("Getting Your Location");
                // Disable the text field when he user has MyLocationService connected
                location_service_enabled = true;
                return location_service_enabled;
            } else {
                view.makeToast("Please turn Locations on");
                return false;
            }
        } else {
            view.locationEnabled(false);
            view.goToLocation(sci_tech);
            view.disableSourceText(false);
            location_service_enabled = false;
            return location_service_enabled;
        }
    }

    @Override
    public void setPic(ImageView mImageView, String filepath){
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }





    /** Data Retrieval **/

    @Override
    public HashMap<String, Vertex> getNodes() {
        return path.getVertices();
    }


    /**
     *
     * @param ll
     */
    @Override
    public void setSource(LatLng ll, int level) {
        start = Distance.find_closest_marker(ll,level ,path.getCNodes());
        isSourceSet = true;
    }


    @Override
    public void cancelPathFinding() {
        path.remove();
        view.removePath();
        unsetSource();
    }


    private void unsetSource(){
        isSourceSet = false;
        start = null;
        view.clearStartText();
    }

    private void unsetDestination(){
        destination = null;
        view.clearDestinationText();
    }

    @Override
    public void destinationArrived() {
        learner.learner1(destination);
        path.remove();
        view.removePath();
    }

    @Override
    public String getPlaceInfo(String id) {

        Place place = (Place) path.getVertices().get(id);

        HashMap<String, String> infoHashMap = place.getInfo();
        String info = "";
        for ( String key: infoHashMap.keySet()
             ) {

            info += key+": "+infoHashMap.get(key)+" \n";

        }
        return info;
    }

    @Override
    public ArrayList<String> getRoomsFromDb() {
        return  dbHelper.roomList();
    }

    @Override
    public Collection<? extends String> getBuildingsFromDb() {
        return dbHelper.buildingList();
    }

    @Override
    public Location onLocationChange(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


        if(location_service_enabled){
            if(onLocationChangedUpdateCamera){
                view.goToLocation(latLng);
            }
        }


        return location;
    }

    @Override
    public void disconncect() {

    }


//    /** Threads Handling **/
//    public static final Handler trackerHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            //arrival message receive from tracker so clear current path
//            Log.d(TAG, "handleMessage: MessageReceived");
//            path.remove();
//            view.deletePath();
//            view.removePOI();
//            //TODO delete starter and destination marker and make is sourceset = flase
//            view.removeStart();
//        }
//
//    };


}
