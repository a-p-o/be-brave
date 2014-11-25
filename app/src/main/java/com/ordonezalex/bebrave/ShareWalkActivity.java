package com.ordonezalex.bebrave;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ordonezalex.bebrave.services.LocationService;
import com.ordonezalex.bebrave.tasks.CreateReportsTask;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.Report;
import com.ordonezalex.bebrave.util.School;
import com.ordonezalex.bebrave.util.Status;
import com.ordonezalex.bebrave.util.User;

public class ShareWalkActivity extends FragmentActivity {
    private final static String TAG = "Be-Brave";

    private MyReceiver receiver;
    private GoogleMap map;
    private GestureDetector gestureDetector;
    public static final int NOTIFICATION_SHARE_WALK_REPORT_ID = 1;
    SupportMapFragment mySupportMapFragment;
    private NotificationManager notificationManager;
    private Notification.Builder builder;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_walk);

        android.support.v4.app.FragmentManager myFragmentManager = getSupportFragmentManager();

        mySupportMapFragment = ((SupportMapFragment) myFragmentManager.findFragmentById(R.id.map));

        map = mySupportMapFragment.getMap();

        receiver = new MyReceiver();

        gestureDetector = new GestureDetector(this,
                new SwipeGestureDetector());

//        setUpMapIfNeeded();

        Button shareWalkButton = (Button) findViewById(R.id.share_walk_button);
        Button stopWalkButton = (Button) findViewById(R.id.stop_walk_button);

        shareWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startShareWalk();
            }
        });

        stopWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent stopLocationServiceIntent = new Intent(ShareWalkActivity.this, LocationService.class);
                stopService(stopLocationServiceIntent);

                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_SHARE_WALK_REPORT_ID);
            }
        });
    }

    private void startShareWalk() {

        // Create Report for Share Walk
        String url = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/report";

        // Get Android school
        School school = new School();
        school.setId(3);

        // Get Emergency alert
        Alert alert = new Alert();
        alert.setId(10006);

        // Get Open status
        Status status = new Status();
        status.setId(2);

        // Get Cupcake User
        User user = new User();
        user.setId(2);

        Report report = new Report();
        report.setUser(user);
        report.setAlert(alert);
        report.setStatus(status);
        report.setSchool(school);

        Log.i(TAG, "About to share walk...");

        // Start location service for share walk report
        new CreateReportsTask(this).execute(report);
        Log.i(TAG, "Sharing walk.");

        // Create notification for Share Walk
        Intent resultIntent = new Intent(ShareWalkActivity.this, ShareWalkActivity.class);

        builder = new Notification.Builder(ShareWalkActivity.this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getResources().getString(R.string.notification_share_walk_title))
                .setContentText(getResources().getString(R.string.notification_share_walk_text))
                .setOngoing(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ShareWalkActivity.this);
        stackBuilder.addParentStack(ShareWalkActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_SHARE_WALK_REPORT_ID, builder.build());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onRightSwipe() {
        // Open profile when swiped left
        Intent intent = new Intent(ShareWalkActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle translateBundle = ActivityOptions.makeCustomAnimation(this, R.anim.activity_right_open_translate, R.anim.activity_right_close_translate).toBundle();
        this.startActivity(intent, translateBundle);
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

    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Right swipe
                if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    ShareWalkActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
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
}
