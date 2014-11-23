package com.ordonezalex.bebrave;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ordonezalex.bebrave.services.LocationService;

public class ShareWalkActivity extends FragmentActivity {
    private final static String TAG = "Be-Brave";

    private MyReceiver receiver;
    private GoogleMap map;
    public static final int NOTIFICATION_SHARE_WALK_ID = 1;
    SupportMapFragment mySupportMapFragment;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_walk);

        android.support.v4.app.FragmentManager myFragmentManager = getSupportFragmentManager();

        mySupportMapFragment = ((SupportMapFragment) myFragmentManager.findFragmentById(R.id.map));

        map = mySupportMapFragment.getMap();

        receiver = new MyReceiver();

//        setUpMapIfNeeded();

        Button shareWalkButton = (Button) findViewById(R.id.share_walk_button);
        Button stopWalkButton = (Button) findViewById(R.id.stop_walk_button);

        shareWalkButton.setOnClickListener(new View.OnClickListener() {
            private Notification.Builder builder = new Notification.Builder(ShareWalkActivity.this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getResources().getString(R.string.notification_share_walk_title))
                    .setContentText(getResources().getString(R.string.notification_share_walk_text))
                    .setOngoing(true)
                    .setPriority(NOTIFICATION_SHARE_WALK_ID);

            @Override
            public void onClick(View view) {

                Intent startLocationServiceIntent = new Intent(ShareWalkActivity.this, LocationService.class);
                startService(startLocationServiceIntent);

                Intent resultIntent = new Intent(ShareWalkActivity.this, ShareWalkActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(ShareWalkActivity.this);
                stackBuilder.addParentStack(ShareWalkActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_SHARE_WALK_ID, builder.build());
            }
        });

        stopWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent stopLocationServiceIntent = new Intent(ShareWalkActivity.this, LocationService.class);
                stopService(stopLocationServiceIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_SHARE_WALK_ID);
            }
        });
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            double latitude = intent.getDoubleExtra("Latitude", 0);
            double longitude = intent.getDoubleExtra("Longitude", 0);

            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("User's Location"));

            // Move the camera instantly to user with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 19));

            // Zoom in, animating the camera.
            // map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            Toast.makeText(ShareWalkActivity.this, "Location of user changed to: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        }
    }

//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (map == null) {
//            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (map != null) {
//                Log.i(TAG, "Map is verified");
//                // The Map is verified. It is now safe to manipulate the map.
//
//            }
//        }
//    }

    @Override
    protected void onResume() {

        Log.i(TAG, "Resumed ShareWalkActivity.");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationService.MY_ACTION);

        Log.i(TAG, "Registering receiver...");

        registerReceiver(receiver, intentFilter);

        Log.i(TAG, "Registered receiver.");

        super.onResume();
    }

    @Override
    protected void onPause() {

        Log.i(TAG, "Paused ShareWalkActivity.");

        if (receiver != null) {
            Log.i(TAG, "Unregistering receiver...");

            unregisterReceiver(receiver);

            Log.i(TAG, "Unregistered receiver.");
        }
        super.onPause();
    }
}
