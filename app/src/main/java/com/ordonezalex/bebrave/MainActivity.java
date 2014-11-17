package com.ordonezalex.bebrave;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.ordonezalex.bebrave.services.LocationService;
import com.ordonezalex.bebrave.tasks.CreateReportsTask;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.Report;
import com.ordonezalex.bebrave.util.School;
import com.ordonezalex.bebrave.util.Status;
import com.ordonezalex.bebrave.util.User;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    public final static String TAG = "BeBrave";

    private SubmitProcessButton alertButton;
    private Button shareWalkButton;
    private Button stopWalkButton;
    //booleans used for the progress button actions
    private boolean pressedUp = false;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertButton = (SubmitProcessButton) findViewById(R.id.alert_button);
        shareWalkButton = (Button) findViewById(R.id.share_walk_button);
        stopWalkButton = (Button) findViewById(R.id.stop_walk_button);

        // Start Alert button
        alertButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG,"PRESSED" );
                        if(pressedUp == false)
                        {
                            pressedUp = true;
                            new ProgressUpTask().execute();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG,"LET GO" );
                        //reset the progress of the button
                        progress = 0;
                        pressedUp = false;
                        alertButton.setProgress(0);
                        alertButton.setBackgroundColor(getResources().getColor(R.color.blue_normal));
                        break;
                }
                return true;
            }
        });

        // Start "Share Walk" buttons
        shareWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the background location service here for testing purposes
                startService(new Intent(LocationService.class.getName()));
            }
        });

        stopWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Stop the background location service here for testing purposes
                stopService(new Intent(LocationService.class.getName()));
            }
        });
        // Stop "Share Walk" buttons
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
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

    //sends a report via asynctask
    private void sendReport()
    {
        // Start using Spring
        String url = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/report";

        // Get Android school
        School school = new School();
        school.setId(4);

        // Get sent from alex alert
        Alert alert = new Alert();
        alert.setID(3);

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

        try {
            Log.i(TAG, "About to send the report");
            String response = new CreateReportsTask().execute(report).get();
            Log.wtf(TAG, response);
        } catch (InterruptedException e) {
//                    Toast.makeText(MainActivity.this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        } catch (ExecutionException e) {
//                    Toast.makeText(MainActivity.this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        }
    }

    //method used to update the progress of the button
    public void UpdateButtonProgress()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progress is used instead of alert.getProgress to avoid recurring loops after progress is at 100
                if(progress == 100)
                {
                    progress+=5;
                    pressedUp=false;
                    sendReport();
                    Log.i(TAG, "Loading done");
                    Toast.makeText(getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    alertButton.setProgress(alertButton.getProgress() + 5);
                    progress+=5;
                }
            }
        });

    }

    class ProgressUpTask extends AsyncTask<Void, Void ,Void>
    {

        @Override
        protected Void doInBackground(Void... Voids) {
            while (pressedUp)
            {
               // Log.i(TAG, "THE BUTTON IS PRESSED");
                UpdateButtonProgress();
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
