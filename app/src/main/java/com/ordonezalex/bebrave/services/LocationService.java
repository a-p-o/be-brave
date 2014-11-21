package com.ordonezalex.bebrave.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();
    private static final long LOCATION_REQUEST_INTERVAL = 0;
    private static final long LOCATION_REQUEST_INTERVAL_FASTEST = 0;

    LocationClient locationClient;
    LocationRequest locationRequest;

    private Timer timer;

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
//            Location testLocation = createLocation(100, 100, 50);
//            locationClient.setMockLocation(testLocation);

            Location newLocation = locationClient.getLastLocation();
            if( newLocation != null)
                Log.i(TAG, "position: " + newLocation.getLatitude() + ", " + newLocation.getLongitude());
            else
                Log.i(TAG, "null");

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

        getLocationUpdates();

        timer = new Timer("LocationServiceTimer");
        timer.schedule(updateTask, 10L, 60 * 100L);
        Log.i(TAG, "Creating location service");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        timer.cancel();
        timer = null;
        Log.i(TAG, "Destroying location service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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

    private void stopLocationUpdates()
    {
        if(locationClient != null && locationClient.isConnected())
        {
            locationClient.removeLocationUpdates(this);
            locationClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Connected to service.");
        Toast.makeText( this,"Connected to service" , Toast.LENGTH_SHORT).show();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL_FASTEST);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(0);

       // locationClient.setMockMode(true);
        locationClient.requestLocationUpdates(locationRequest, this , getMainLooper());



    }

    @Override
    public void onDisconnected() {

        Log.e(TAG, "Disconnected");
        stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            Log.i(TAG,"position: " + location.getLatitude() + ", " + location.getLongitude());
            Toast.makeText(this, "position: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.i(TAG, "Location is null");

            Toast.makeText( this,"Location is null" , Toast.LENGTH_SHORT).show();
        }

        Log.i(TAG, "Location should be tracked gg ");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "ConnectionFailed");
        stopLocationUpdates();
        stopSelf();

    }

    public Location createLocation( double lat , double lng, float accuracy)
    {
        Location newLocation = new Location("be-brave");
        newLocation.setLatitude(lat);
        newLocation.setLongitude(lng);
        newLocation.setAccuracy(accuracy);
        return newLocation;
    }
}

