package com.ordonezalex.bebrave.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();
    private LocationManager lm;

    // Declare the timer for periodic task running
    private Timer timer;

    // Set up the timer task to run a specific task
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {

            getLocationUpdates();
            Log.i(TAG, "Timer task doing work");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "Service Creating");

        timer = new Timer("LocationServiceTimer");
        timer.schedule(updateTask, 10L, 60 * 100L);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i(TAG, "Service Destroying");

        timer.cancel();
        timer = null;
    }

    public void getLocationUpdates() {

        MyLocationListener locList = new MyLocationListener();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locList, getMainLooper());

        setMockLocation(15.387653, 73.872585, 500);
    }

    private void setMockLocation(double latitude, double longitude, float accuracy) {

        lm.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);
        newLocation.setTime(1000L);
        newLocation.setElapsedRealtimeNanos(100L);

        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        lm.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null, System.currentTimeMillis());

        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
    }
}

class MyLocationListener implements LocationListener {
    public String TAG = "beBrave";

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "Latitude: " + location.getLatitude());
        Log.i(TAG, "Longitude: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        Log.i(TAG, "GPS Enabled");
    }

    @Override
    public void onProviderDisabled(String s) {

        Log.i(TAG, "GPS Disabled");
    }
}

