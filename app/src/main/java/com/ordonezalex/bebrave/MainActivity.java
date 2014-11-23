package com.ordonezalex.bebrave;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.iml.SubmitProcessButton;
import com.ordonezalex.bebrave.dialogs.CancelReportDialogFragment;
import com.ordonezalex.bebrave.tasks.CreateReportsTask;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.Report;
import com.ordonezalex.bebrave.util.School;
import com.ordonezalex.bebrave.util.Status;
import com.ordonezalex.bebrave.util.User;

public class MainActivity extends Activity {
    public static final String TAG = "BeBrave";


    private SubmitProcessButton reportButton;
    private GestureDetector gestureDetector;
    //booleans used for the progress button actions
    private boolean pressedUp = false;
    private int progress;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this,
                new SwipeGestureDetector());

        reportButton = (SubmitProcessButton) findViewById(R.id.report_button);


        // Start Alert button
        reportButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "PRESSED");
                        if (pressedUp == false) {
                            pressedUp = true;
                            new ProgressUpTask().execute();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "LET GO");
                        //reset the progress of the button
                        progress = 0;
                        pressedUp = false;
                        reportButton.setProgress(0);
                        reportButton.setBackgroundColor(getResources().getColor(R.color.blue_normal));
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    //method for swiping left
    private void onLeftSwipe() {
        // Open profile when swiped left
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void onRightSwipe() {
        // Open share walk when swiped right
        Intent intent = new Intent(MainActivity.this, ShareWalkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        } else if (id == R.id.action_profile) {
            openProfile();
            return true;
        } else if (id == R.id.action_alert) {
            openAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void openSettings() {

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void openAlert() {

        Intent intent = new Intent(MainActivity.this, AlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void cancelReportDialog() {

        Log.i(TAG, "Creating cancel report dialog.");
        DialogFragment newFragment = new CancelReportDialogFragment();
        newFragment.show(getFragmentManager(), "CancelReport");
        Log.i(TAG, "Showed cancel report dialog.");
    }

    //sends a report via asynctask
    private void sendReport() {
        // Start using Spring
        String url = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/report";

        // Get Android school
        School school = new School();
        school.setId(4);

        // Get sent from alex alert
        Alert alert = new Alert();
        alert.setPriority(1);

        // Get Help me status
        Status status = new Status();
        status.setId(1);

        // Get textUser User
        User user = new User();
        user.setId(1);

        Report report = new Report();
        report.setUser(user);
        report.setAlert(alert);
        report.setStatus(status);
        report.setSchool(school);

        Log.i(TAG, "About to send the report...");
        new CreateReportsTask().execute(report);
        Log.i(TAG, "Report sent.");
    }

    //method used to update the progress of the button
    public void updateButtonProgress() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progress is used instead of alert.getProgress to avoid recurring loops after progress is at 100
                if (progress == 100) {
                    progress += 5;
                    pressedUp = false;
                    cancelReportDialog();
                    Log.i(TAG, "Loading done");
                } else {
                    reportButton.setProgress(reportButton.getProgress() + 5);
                    progress += 5;
                }
            }
        });
    }

    //private class for determining left or right swipe gestures
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

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MainActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MainActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

    class ProgressUpTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if (progress >= 100) {
                sendReport();
                Toast.makeText(getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... Voids) {

            while (pressedUp) {
                // Log.i(TAG, "THE BUTTON IS PRESSED");
                updateButtonProgress();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
