package com.example.eventracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class UserLocation extends AppCompatActivity {
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

    private FusedLocationProviderClient mFusedLocationClient;

    private double mLatitude = 0.0, mLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private android.widget.Button btnLocation;
    private TextView txtLocation;


    private boolean isGPS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        this.txtLocation = (TextView) findViewById(R.id.txtLocation);
        this.btnLocation = (Button) findViewById(R.id.btnLocation);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        if (!isGPS) {
            Toast.makeText(UserLocation.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        UserLocation.this.getLocation();

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("long", String.valueOf(mLongitude));
                i.putExtra("lat", String.valueOf(mLatitude));
                setResult(RESULT_OK, i);
                finish();


            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        txtLocation.setText(String.format(Locale.US, "%s, %s", mLatitude, mLongitude));

                    } else {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                                Log.d("Loc1", "location" + mLatitude);
                                txtLocation.setText(String.format(Locale.US, "%s - %s", mLatitude, mLongitude));
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }
}