package com.uwimonacs.fstmobile.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private String location;
    private String department;
    private String shortname;
    private String fullname;
    private List<Place> places;
    private HashMap<String, Integer> colors;
    private DirectionsRoute currentRoute;

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

                //final Position origin = Position.fromCoordinates(-76.750189,18.005988); //For Testing
                final Position origin = Position.fromCoordinates(map.getMyLocation().getLongitude(), map.getMyLocation().getLatitude());
                final Position destination = Position.fromCoordinates(lon,lat);

                map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                    @Override
                    public boolean onInfoWindowClick(@NonNull Marker marker) {
                        try{
                            getRoute(origin, destination);
                        }catch(ServicesException e)
                        {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

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

    private void getRoute(Position origin, Position destination) throws ServicesException {

        MapboxDirections client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setAccessToken(getString(R.string.access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(MapActivity.this, "No routes found", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    currentRoute = response.body().getRoutes().get(0);
                    Toast.makeText(MapActivity.this, "Route is " + currentRoute.getDistance() + " meters long.", Toast.LENGTH_SHORT).show();

                    // Draw the route on the map
                    drawRoute(currentRoute);
                }catch(Exception e)
                {
                    Toast.makeText(MapActivity.this, "Cannot Find Route", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        map.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#009688"))
                .width(5));
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