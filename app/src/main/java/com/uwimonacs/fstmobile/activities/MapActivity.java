package com.uwimonacs.fstmobile.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap map;
    private String location;
    private String department;
    private String shortname;
    private String fullname;
    private List<Place> places;
    private HashMap<String, Integer> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();

        location = extras.getString("location");
        department = extras.getString("department");
        shortname = extras.getString("shortname");
        fullname = extras.getString("fullname");
        places = (ArrayList<Place>)(getIntent().getBundleExtra("placesList").getSerializable("places"));

        colors = new HashMap<>();

        colors.put("Engineering",Color.parseColor("#654321"));
        colors.put("Physics",Color.parseColor("#3bb2d0"));
        colors.put("Life Sciences",Color.parseColor("#ffa500"));
        colors.put("Computing",Color.parseColor("#551A8B"));
        colors.put("Chemistry",Color.parseColor("#FF69B4"));

        mapView = (MapView) findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                mapboxMap.setMyLocationEnabled(true);

                final String[] locals = location.split(",");
                final String snip = department + " - " + shortname;

                final double lat = Double.parseDouble(locals[0]);
                final double lon = Double.parseDouble(locals[1]);

                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(new LatLng(lat,lon)).build()));

                //For target location
                map.addMarker(new MarkerViewOptions()
                        .position(new LatLng(lat,lon))
                        .title(fullname)
                        .snippet(snip));

                for (Place place : places) {
                    if (place.getFullname().contains("Building")) {
                        try {
                            drawBuilding(map, place.getLocation(), colors.get(place.getDepartment()));
                        }
                        catch (Exception e){ e.printStackTrace();}
                    }
                }
            }
        });
    }

    private void drawBuilding(MapboxMap mapboxMap, String coord, int color) {

        String[] newCoords = coord.split(",");
        ArrayList<String> even = new ArrayList<>();
        ArrayList<String> odd = new ArrayList<>();
        List<LatLng> polygon = new ArrayList<>();

        for(int c=0; c < newCoords.length; c++) {
            if(c%2 == 0){
                even.add(newCoords[c]);
            }
            else{
                odd.add(newCoords[c]);
            }
        }

        for(int c=0; c < odd.size(); c++) {
            polygon.add(new LatLng(Double.parseDouble(even.get(c)), Double.parseDouble(odd.get(c))));
        }

        mapboxMap.addPolygon(new PolygonOptions()
                .addAll(polygon)
                .fillColor(color));

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}