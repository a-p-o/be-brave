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
import com.ordonezalex.bebrave.tasks.UpdateReportsTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();
    private static final long LOCATION_REQUEST_INTERVAL = 1000;
    private static final long LOCATION_REQUEST_INTERVAL_FASTEST = 1000;
    private static final float LOCATION_REQUEST_DISPLACEMENT_MINIMUM = 0f;
    public static final String EXTRA_REPORT_ID = "EXTRA_REPORT_ID";

    private int reportId;
    private LocationClient locationClient;
    private LocationRequest locationRequest;

    public static final String MY_ACTION = "MY_ACTION";

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Log.i(TAG, "Created location service");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        stopLocationUpdates();

        Log.i(TAG, "Destroying location service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Starting service command...");

        Log.i(TAG, "Setting report ID...");

        setReportId(intent.getIntExtra(EXTRA_REPORT_ID, -1));

        Log.i(TAG, "Report ID set. ID is " + reportId);

        getLocationUpdates();

        return Service.START_STICKY;
    }

    private void getLocationUpdates() {

        Log.i(TAG, "Receiving new location from client.");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(this, this, this);

            if (!locationClient.isConnected() || !locationClient.isConnecting()) {

                Log.i(TAG, "Connecting client...");
                locationClient.connect();
            }
        } else {
            Log.wtf(TAG, "Cannot connect to Google Play Services.");
        }
    }

    private void stopLocationUpdates() {

        Log.i(TAG, "Stopping client...");

        if (locationClient != null && locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);

            Log.i(TAG, "Disconnecting client...");

            locationClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Connected client.");
        Toast.makeText(this, "Connected client.", Toast.LENGTH_SHORT).show();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL_FASTEST);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(LOCATION_REQUEST_DISPLACEMENT_MINIMUM);

        locationClient.requestLocationUpdates(locationRequest, this, getMainLooper());
    }

    @Override
    public void onDisconnected() {

        Log.i(TAG, "Disconnected client.");
        stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "New location received by client.");

        if (location != null) {
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("Latitude", location.getLatitude());
            intent.putExtra("Longitude", location.getLongitude());

            sendBroadcast(intent);

            Log.i(TAG, "About to update the location...");

            DateFormat format = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy");
            Date date = new Date(location.getTime());
            String time = format.format(date);

            com.ordonezalex.bebrave.util.Location updateLocation = new com.ordonezalex.bebrave.util.Location(location.getLatitude(), location.getLongitude(), time);
            new UpdateReportsTask(reportId).execute(updateLocation);
            Log.i(TAG, "Location updated.");

            Log.i(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude());
            //Toast.makeText(this, "position: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Location is null");

            Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "Connection to Google Play Serviced failed.");

        stopLocationUpdates();
        stopSelf();
    }

    public void setReportId(int reportId) {

        this.reportId = reportId;
    }
}

