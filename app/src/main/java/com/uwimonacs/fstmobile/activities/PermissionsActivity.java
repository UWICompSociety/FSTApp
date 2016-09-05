package com.uwimonacs.fstmobile.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;

public class PermissionsActivity extends AppCompatActivity {
    private static final int PERMISSIONS_LOCATION = 0;
    private AppCompatActivity activity = this;
    private static final int REQUEST_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        if(!checkPermissions())
            showHelpDialog();
        else
            startMapActivity();
    }

    private boolean checkPermissions(){
        return !((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED));
    }

    private void requestPermissions(){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSIONS_LOCATION);
    }

    private void showHelpDialog() {
        final String message = "We need your permission to access your location so we can give you " +
                "directions to the room you're looking for. Select OK to proceed.";

        final AlertDialog dialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                activity.finish();
            }
        });
        dialog.show();
    }

    private void startMapActivity(){

      //  final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        //if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        //{
          //  buildAlertMessageNoGps();
        //}else{
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtras(getIntent());
            startActivity(intent);
            finish();
       // }



    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),REQUEST_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startMapActivity();
                else
                    finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == 0){
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(provider != null){
                startMapActivity();
            }else{
                Toast.makeText(this,"Gps Needs to be enabld",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
