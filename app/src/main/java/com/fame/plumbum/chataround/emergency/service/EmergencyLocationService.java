package com.fame.plumbum.chataround.emergency.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.fame.plumbum.chataround.emergency.OnLocationUpdateResponse;
import com.fame.plumbum.chataround.emergency.model.UpdateSosData;
import com.fame.plumbum.chataround.emergency.provider.RetrofitUpdateSosProvider;
import com.fame.plumbum.chataround.emergency.provider.UpdateSosHelper;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by meghal on 22/5/17.
 */

public class EmergencyLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private static final String TAG = "EmergencyService";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private String sosId="";
    private UpdateSosHelper updateSosHelper;
    private SharedPrefs sharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPrefs = new SharedPrefs(this);
        updateSosHelper = new RetrofitUpdateSosProvider();

        Toast.makeText(this, "On Create Service", Toast.LENGTH_SHORT).show();


        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {

       /* if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }*/

        Toast.makeText(this, "On Start Service", Toast.LENGTH_SHORT).show();


        sosId = intent.getExtras().getString(Keys.KEY_SOS_ID);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();

        Log.d(TAG, "onStart");

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            Toast.makeText(this, "On No permission", Toast.LENGTH_SHORT).show();

            // for ActivityCompat#requestPermissions for more details.
            return;

        }
//        registerReceiver(new PhoneUnlockedReceiver(), new IntentFilter("android.intent.action.USER_PRESENT"));
//        registerReceiver(new PhoneUnlockedReceiver(), new IntentFilter("android.intent.action.SCREEN_OFF"));
/*        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 0, listener);
            Toast.makeText(this, "WIFI location available", Toast.LENGTH_SHORT).show();


        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, listener);
            Toast.makeText(this, "Network location available", Toast.LENGTH_SHORT).show();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);
            Toast.makeText(this, "GPS location available", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Locations Services not available", Toast.LENGTH_SHORT).show();

        }*/

//        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 0, listener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, listener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

        Toast.makeText(this, "On destroy service", Toast.LENGTH_SHORT).show();

//        locationManager.removeUpdates(listener);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(EmergencyLocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EmergencyLocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "NO PERMISSION", Toast.LENGTH_SHORT).show();

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {


            try {

                updateSosHelper.updateSos(sharedPrefs.getUserId(), sosId, location.getLatitude(), location.getLongitude(), new OnLocationUpdateResponse() {

                    @Override
                    public void onSuccess(UpdateSosData updateSosData) {

                        Log.d(TAG, updateSosData.getMessage());

                    }

                    @Override
                    public void onFailed(String message) {
                        Log.d(TAG, message);

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "ON CONNECT SUSPENDED", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "ON CONNECT FAILURE", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();


        updateSosHelper.updateSos(sharedPrefs.getUserId(), sosId, location.getLatitude(), location.getLongitude(), new OnLocationUpdateResponse() {

            @Override
            public void onSuccess(UpdateSosData updateSosData) {

                Log.d(TAG, updateSosData.getMessage());

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, message);

            }
        });
    }

/*
    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location location) {

            Log.d(TAG, "onLocationChanged\n Latitude" + String.valueOf(location.getLatitude()) + "\nLongitude : " + String.valueOf(location.getLongitude()));

           *//* updateSosHelper.updateSos(sharedPrefs.getUserId(), sosId, location.getLatitude(), location.getLongitude(), new OnLocationUpdateResponse() {

                @Override
                public void onSuccess(UpdateSosData updateSosData) {

                    Log.d(TAG, updateSosData.getMessage());

                }

                @Override
                public void onFailed(String message) {
                    Log.d(TAG, message);

                }
            });*//*

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), provider+" Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), provider+" Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (ActivityCompat.checkSelfPermission(EmergencyLocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EmergencyLocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 2000, 0, listener);
            Toast.makeText(getApplicationContext(), provider+" Status Changed to this provider", Toast.LENGTH_SHORT).show();


        }

    */

}
