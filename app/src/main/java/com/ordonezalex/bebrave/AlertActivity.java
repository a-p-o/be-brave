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
    private List<String> titles;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/alert";
    private long userSchoolId = 4;//will be pulled from user

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        alertListView = (ListView) findViewById(R.id.alerts_list_view);
        populateAlerts();
    }

    private void populateAlerts() {

//        alerts = new Alert[0];

        try {
            alerts = new GetAlertsTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        titles = new ArrayList<String>();
        int count = 0;

        //Filter out alerts from other schools
        for (int i = 0; i < alerts.length; i++) {
            if (alerts[i].getSchool().getId() == userSchoolId) {
                titles.add(alerts[i].getTitle());
                count++;
            }
        }

        //add alerts to adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        alertListView.setAdapter(adapter);
    }
}

