package com.ordonezalex.bebrave;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ordonezalex.bebrave.services.LocationService;

public class ShareWalkActivity extends FragmentActivity {
    private final static String TAG = "Be-Brave";

    private MyReceiver receiver;
    private GoogleMap map;
    private Button shareWalkButton;
    private Button stopWalkButton;
    public static final int NOTIFICATION_SHARE_WALK_ID = 1;
    private android.support.v4.app.FragmentManager myFragmentManager;
    SupportMapFragment mySupportMapFragment;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_walk);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_placeholder, new PlaceholderFragment())
//                    .commit();
//        }

        myFragmentManager = getSupportFragmentManager();

        mySupportMapFragment = ((SupportMapFragment) myFragmentManager.findFragmentById(R.id.map));

        map = mySupportMapFragment.getMap();

        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationService.MY_ACTION);
        registerReceiver(receiver, intentFilter);

        //setUpMapIfNeeded();

        shareWalkButton = (Button) findViewById(R.id.share_walk_button);
        stopWalkButton = (Button) findViewById(R.id.stop_walk_button);


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
            double Latitude = intent.getDoubleExtra("Latitude", 0);
            double Longitude = intent.getDoubleExtra("Longitude", 0);

            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(Latitude,Longitude))
                    .title("User's Location"));

            // Move the camera instantly to user with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitude), 19));

            // Zoom in, animating the camera.
           // map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            Toast.makeText(ShareWalkActivity.this, "Location of user changed to: " + Latitude + ", " + Longitude, Toast.LENGTH_SHORT).show();
        }
    }

//    public static class PlaceholderFragment extends Fragment {
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            View rootView = inflater.inflate(R.layout.fragment_share_walk, container, false);
//            return rootView;
//        }
//    }

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
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }
}
