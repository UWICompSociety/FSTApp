package com.uwimonacs.fstmobile.services;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uwimonacs.fstmobile.fragments.mapFragment.MapPresenter;
import com.uwimonacs.fstmobile.models.locations.Edge;
import com.uwimonacs.fstmobile.models.locations.Vertex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by Kyzer on 5/12/2017.
 * this class does the managing of the polylines used to diplay the grapth and the paths on the map
 *
 */

public class MapPolylines {
    private static final String TAG = "com.android.comp3901";

    LinkedList<Polyline> groundFloor;
    LinkedList<Polyline> firstFloor;
    LinkedList<Polyline> secondFloor;
    LinkedList<Polyline> descendingEdges;




    private final GoogleMap googleMap;
    LinkedList<Polyline> currPath = null;

    public static MapPolylines getInstance(GoogleMap map){
        if(instance == null){
            instance = new MapPolylines(map);
        }
        return instance;
    }

    private  static MapPolylines instance;

    private MapPolylines(GoogleMap map) {

        this.googleMap = map;
        this.groundFloor = new LinkedList<>();
        this.firstFloor = new LinkedList<>();
        this.secondFloor = new LinkedList<>();
        this.descendingEdges = new LinkedList<>();
        this.currPath = new LinkedList<>();

    }


    public void createGraph(){
        List<Edge> edges = MapPresenter.path.getEdges();
//        List<Vertex> vertices = MapFrag.path.getNodes();

        HashMap<String, Vertex> vertexHashMap = MapPresenter.path.getVertices();
        Polyline lane;
        Vertex v1,v2;


        for(Edge edge: edges){


            v1 = vertexHashMap.get(edge.getSource().getId());
            v2 = vertexHashMap.get(edge.getDestination().getId());



            //TODO make polyline clickable
            PolylineOptions options = new PolylineOptions()
                    .color(0x33606060)
                    .width(15)
                    .zIndex(01)
                    .clickable(true)
                    .add(v1.getLL(),v2.getLL());

            lane =  googleMap.addPolyline(options);
            lane.setClickable(true);

//            Log.d(TAG, "createGraph: edge level : "+ edge.getLevel());

            //TODO fix the colors schemes for the polylines
            switch (edge.getLevel()){
                case 0:
                    lane.setColor(Color.MAGENTA);
                    lane.setVisible(false);
                    descendingEdges.add(lane);
                    break;
                case 1:
                    lane.setColor(0x33606060);//
                    groundFloor.add(lane);
                    break;
                case 2:
                    lane.setColor(Color.YELLOW);
                  lane.setVisible(false);
                    firstFloor.add(lane);
                    break;
                case 3:
                    lane.setColor(Color.CYAN);
                    lane.setVisible(false);
                    secondFloor.add(lane);
                    break;
                default:
                    Log.d(TAG, "createGraph: NO LEVEL");
                    break;
            }
        }
    }

    public void createPath(LinkedList<Vertex> route){

          if(!currPath.isEmpty())
              deletePath();

            route.size();
            LinkedList<LatLng> pnts = new LinkedList<>();

            for( int i = 0; i<route.size()-1; i++ ){
                LatLng  ll =  route.get(i).getLL();
                LatLng  ll2 =  route.get(i+1).getLL();
                PolylineOptions  options = new PolylineOptions()
                .width(5)
                .add(ll, ll2 )
                .zIndex(02);

                if(route.get(i).getLevel()==3){
                    options.color(Color.MAGENTA);
                }else if(route.get(i).getLevel()==2){
                    options.color(Color.YELLOW);
                }
                else{
                 options.color(Color.GREEN);

        }

                    Polyline line = googleMap.addPolyline(options);
                    currPath.add(line);
                }
        }

        public void showAllLevels(){

        }


    public void showLevelTwo(){

    }


    public void deletePath() {
        for (Polyline line:
             currPath) {
            line.remove();
        }
        currPath.clear();
    }



    public void setGroundFloor(boolean checked){
        for(Polyline line: groundFloor){
            line.setVisible(checked);
        }
    }

    public void setFirstFloor(boolean checked) {
         for(Polyline line: firstFloor){
            line.setVisible(checked);
        }
    }

    public void setSecondFloor(boolean checked) {
        for(Polyline line: secondFloor){
            line.setVisible(checked);
        }
    }

    public boolean groundFloorIsVisilble() {
        for(Polyline line: groundFloor){
            return line.isVisible();
        }

        return false;
    }

    public boolean secondFloorIsVisible() {
        for(Polyline line: secondFloor){
            return line.isVisible();
        }

        return false;

    }

    public boolean firstFloorIsVisible() {
        for(Polyline line: firstFloor){
            return line.isVisible();
        }

        return false;
    }
}
