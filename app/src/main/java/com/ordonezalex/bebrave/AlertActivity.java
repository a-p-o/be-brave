package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ordonezalex.bebrave.tasks.GetAlertsTask;
import com.ordonezalex.bebrave.util.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlertActivity extends Activity {

    public final static String TAG = "BeBrave";

    private ListView alertListView;
    private Alert[] alerts;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/alert";
    private long userSchoolId = 4; // Will be pulled from user

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        alertListView = (ListView) findViewById(R.id.alerts_list_view);
        populateAlerts();
    }

    private void populateAlerts() {

        List<String> titles = new ArrayList<String>();

        try {
            alerts = new GetAlertsTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Filter out Alerts from other schools
        for (Alert alert : alerts) {
            if (alert.getSchool().getId() == userSchoolId) {
                titles.add(alert.getTitle());
            }
        }

        // Add Alerts to adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        alertListView.setAdapter(adapter);
    }
}

