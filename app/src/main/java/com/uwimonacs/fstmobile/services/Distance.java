package com.uwimonacs.fstmobile.services;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uwimonacs.fstmobile.models.locations.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kyzer on 3/14/2017.
 * This class will provide all the calculation in regards to the distances between points
 */




public class Distance {

    public static Double R = 6371.00; // radius of earth in km


    public Distance(){}




    /**
     *   Find the total distance of a route
     */
    public static Double routeDistance(LinkedList<Vertex> route){
        double dist = 0.00;

        for(int i=0; i < route.size()-1 ; i++ ){

            Vertex current = route.get(i);
            Vertex next = route.get(i+1);


            dist = dist + find_distance(current.getLL(), next.getLL());
        }

        return dist;
    }


    /**
     * Uses Harversine's formula to calculate the distance between two LatLng
     * @param myLL the Lat long provided by your location
     * @param markerLL
     * @return returns the distance d in Km
     */
    public static Double find_distance(LatLng myLL, LatLng markerLL){

        double myLat = myLL.latitude;
        double myLng = myLL.longitude;

        double mlat = markerLL.latitude;
        double mlng = markerLL.longitude;



        Double dLat = rad(mlat - myLat);
        Double dLong = rad(mlng - myLng);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(myLat)) * Math.cos(rad(myLat)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c;

        Log.d("Distance  ",""+d);
        return d;
    }


    /**
     * Method is used to find the closest point on the graph
     * given the starting point
     * @param LL your current latlng
     * @param myLevel your level that the the vertex is on.
     * @param points the point that are connected to the graph
     * @return
     */
    public static Vertex find_closest_marker(LatLng LL, int myLevel, List<Vertex> points){

        Vertex closest_point;
        ArrayList<Double> distances = new ArrayList();
        Integer closest = -1;

        for (int i = 0; i < points.size(); i++) {
            //Marker points Lat & Lng
            LatLng markerLL = points.get(i).getLL();
            double d = find_distance(LL,markerLL);
            distances.add(d);

            if(myLevel == points.get(i).getLevel()){

                if (closest == -1 || d < distances.get(closest)) {
                    closest = i;
                }
            }
        }


        closest_point = points.get(closest);
        return closest_point;
    }


    /**
     * this method find the closest vertex to the user based of the type of marker requested
     * @param LL
     * @param points
     * @param type the type of vertex
     * @return
     */
    public static Vertex find_closest_marker(LatLng LL, List<Vertex> points, String type){

        Vertex closest_point;
        ArrayList<Double> distances = new ArrayList();
        Integer closest = -1;

        for (int i = 0; i < points.size(); i++) {
            //Marker points Lat & Lng
            LatLng markerLL = points.get(i).getLL();
            double d = find_distance(LL,markerLL);
            distances.add(d);

            if(type == points.get(i).getType()){

                if (closest == -1 || d < distances.get(closest)) {
                    closest = i;
                }
            }
        }


        closest_point = points.get(closest);
        return closest_point;
    }


    private static double rad(double x) {
        return x * Math.PI / 180;
    }



}// End of distance Class
