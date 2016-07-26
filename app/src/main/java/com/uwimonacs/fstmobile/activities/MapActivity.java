package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private Place place = (Place) getIntent().getSerializableExtra("Place"); //get Place from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                mapboxMap.setMyLocationEnabled(true);

                String[] locals = place.getLocation().split(",");
                String snip = place.getDepartment() + " - " + place.getShortname();

                map.addMarker(new MarkerViewOptions()
                        .position(new LatLng(Double.parseDouble(locals[0]),Double.parseDouble(locals[1])))
                        .title(place.getFullname())
                        .snippet(snip));
            }
        });
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
