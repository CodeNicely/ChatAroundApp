package com.fame.plumbum.chataround.models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pankaj on 24/7/16.
 */
public class LocationChanges implements LocationListener{
    private LocationManager locationManager;
    private String provider;
    boolean isGPSEnabled=false, isNetworkEnabled=false;
    double lat, lng;
    Activity activity;

    public LocationChanges(Activity activity){
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
    }

    public boolean isGPSEnabled() {
        try {
            if (locationManager == null)
            locationManager = (LocationManager)activity.getApplicationContext().getSystemService(activity.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.e("Error", "GPS");
            //toast("Exception " + e.toString());
        }
        return isGPSEnabled;
    }

    public boolean isWIFIEnabled() {
        try {
            if (locationManager == null)
            locationManager = (LocationManager)activity.getApplicationContext().getSystemService(activity.LOCATION_SERVICE);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e("Error", "WiFi");
//            toast("Exception " + e.toString());
        }
        return isNetworkEnabled;
    }

    public double[] getLocation(){
        // Get the location manager
        // Define the criteria how to select the locatioin provider -> use
        // default

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return new double[0];
        }
        Location location = locationManager.getLastKnownLocation(provider);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        // Initialize the location fields
        if (!isGPSEnabled() && !isWIFIEnabled())
            Toast.makeText(activity, "Please turn on GPS!", Toast.LENGTH_SHORT).show();
        if (location != null) {
            Log.e("Provider", provider + " has been selected.");
            onLocationChanged(location);
            return new double[]{lat, lng};
        }
        return new double[]{0};
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = Math.round(location.getLatitude()*100)/100;
        lng = Math.round(location.getLongitude()*100)/100;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
