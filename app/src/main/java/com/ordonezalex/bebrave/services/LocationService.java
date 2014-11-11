package com.ordonezalex.bebrave.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sebastian Florez on 11/10/2014.
 */
public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();

    private Timer timer;

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "Timer task doing work" );
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
        timer.schedule(updateTask, 10L, 60*100L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service Destroying");

        timer.cancel();
        timer = null;
    }
}
