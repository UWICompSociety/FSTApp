package com.uwimonacs.fstmobile.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.uwimonacs.fstmobile.R;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private Bundle extras;
    private String location;
    private String department;
    private String shortname;
    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();

        location = extras.getString("location");
        department = extras.getString("department");
        shortname = extras.getString("shortname");
        fullname = extras.getString("fullname");

        mapView = (MapView) findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                mapboxMap.setMyLocationEnabled(true);

                String[] locals = location.split(",");
                String snip = department + " - " + shortname;

                double lat = Double.parseDouble(locals[0]);
                double lon = Double.parseDouble(locals[1]);

                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(new LatLng(lat,lon)).build()));

                //For target location
                map.addMarker(new MarkerViewOptions()
                        .position(new LatLng(lat,lon))
                        .title(fullname)
                        .snippet(snip));

                //For user location
                IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MapActivity.this, R.drawable.blue_marker);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                map.addMarker(new MarkerViewOptions()
                        .position(new LatLng(map.getMyLocation()))
                        .title("You")
                        .icon(icon));
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
