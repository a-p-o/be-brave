package com.ordonezalex.bebrave.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationService extends Service implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();
    private static final long LOCATION_REQUEST_INTERVAL = 10000;
    private static final long LOCATION_REQUEST_INTERVAL_FASTEST = 5000;

    LocationClient locationClient;
    LocationRequest locationRequest;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "Creating location service");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i(TAG, "Destroying location service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.getLocationUpdates();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getLocationUpdates() {

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(this, this, this);

            if (!locationClient.isConnected() || !locationClient.isConnecting()) {
                locationClient.connect();
            }
        } else {
            Log.wtf(TAG, "Cannot connect to Google Play Services. Check your network connection.");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Connected to service.");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL_FASTEST);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

