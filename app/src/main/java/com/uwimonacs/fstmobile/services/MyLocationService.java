package com.uwimonacs.fstmobile.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kyzer on 5/24/2017.
 */

public class MyLocationService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = "Locations Class: ";
    public static final float MIN_ACCURACY = 13;

    public Location my_location;
    private GoogleApiClient mGoogleApiClient;
    private static Context ctx;
    private LocationRequest mLocationRequest;
    private static MyLocationService instance = null;

    private myLocationCallback locationCallback;

    public static MyLocationService getInstance(Context ctx)  {
        if (instance == null) {
            instance = new MyLocationService(ctx);
        }
        return instance;
    }

    private MyLocationService(Context ctx) {
        this.ctx = ctx;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .enableAutoManage((FragmentActivity) ctx, this)
                    .build();
        }
        mGoogleApiClient.connect();

    }

    /***
     * Checks to see if the user has location services enabled returns false if it is off.
     * @return
     */
    public boolean isGPSEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(ctx.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;  // Return true if the location services is turn on

        } else {
            locationProviders = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public Location getLocation() {

//
//        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null;
//        }
//        my_location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        return my_location;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //creates a location request object that gets the users location
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); //make a  request for the user's location every 1 second
        try{
            if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }catch(NullPointerException e){

        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        my_location = location;
            if(location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
            }else if(location.hasAltitude()){
                double altitude = location.getAltitude();
                Log.d(TAG, "onConnected: Altitude; "+ altitude);
            }else{
                Toast.makeText(ctx, "if your inside a building please select floor", Toast.LENGTH_SHORT);
            }
            Log.d(TAG, "onConnected: "+ location);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //this methods uses the location services to display users location
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            Toast.makeText(ctx, "Cant get current Location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            //TODO call tracker method
            my_location = location;
            Log.d(TAG, "onLocationChanged: Location Accuracy: " + location.getAccuracy());
            Log.d(TAG, "onLocationChanged: " + String.valueOf(location));

            try{
                locationCallback.onLocationChange(location);
            }catch(NullPointerException e){}

        }

    }


    /**
     * Disconnes the google api client
     */
    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    /**
     * Method checks to see if the user's current location is accurate;
     * @return true if the accuraccy given is less than the user or equal to the the minimum specified
     */
    public boolean isAccurate() {

        float accuracy = getLocation().getAccuracy();
        return accuracy<=MIN_ACCURACY;
    }


    /**
     * interface used to get call back whenever the location is changed
     */
    public interface myLocationCallback{
        Location onLocationChange(Location location);
        void disconncect();
    }

    public void setOnLocationChangedListner(myLocationCallback mListner){
        this.locationCallback = mListner;
    }




}
