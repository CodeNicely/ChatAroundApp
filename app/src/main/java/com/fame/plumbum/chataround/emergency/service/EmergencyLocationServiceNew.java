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

import com.fame.plumbum.chataround.emergency.OnLocationUpdateResponse;
import com.fame.plumbum.chataround.emergency.model.UpdateSosData;
import com.fame.plumbum.chataround.emergency.provider.RetrofitUpdateSosProvider;
import com.fame.plumbum.chataround.emergency.provider.UpdateSosHelper;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by meghal on 30/7/17.
 */
public class EmergencyLocationServiceNew extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public static final String BROADCAST_ACTION = "Hello ShoutsFragmentOld";
    private static final String TAG = "ELocationServiceNew";
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    public double lat, lng;
    LocationRequest mLocationRequest;
    private String sosId = "";
    private UpdateSosHelper updateSosHelper;
    private SharedPrefs sharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPrefs = new SharedPrefs(this);
        updateSosHelper = new RetrofitUpdateSosProvider();

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sosId = intent.getExtras().getString(Keys.KEY_SOS_ID);

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }


        mGoogleApiClient.connect();
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        sharedPrefs.setSosOngoing(false);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();

            updateSosHelper.updateSos(sharedPrefs.getUserId(), sosId, lat, lng, new OnLocationUpdateResponse() {

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
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       /* mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(1000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);*/

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        if (sharedPrefs.isSosOngoing()) {
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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }
}