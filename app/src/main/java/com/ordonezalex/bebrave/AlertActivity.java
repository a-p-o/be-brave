package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ordonezalex.bebrave.tasks.CreateReportsTask;
import com.ordonezalex.bebrave.tasks.GetAlertsTask;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.Report;
import com.ordonezalex.bebrave.util.School;
import com.ordonezalex.bebrave.util.Status;
import com.ordonezalex.bebrave.util.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlertActivity extends Activity {

    public final static String TAG = "BeBrave";
    private  List<String> titles;
    private ListView alertListView;
    private Alert[] alerts;
    private static long userSchoolId = 4; // Will be pulled from user
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/alert?School=" + userSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        alertListView = (ListView) findViewById(R.id.alerts_list_view);
        populateAlerts();
        alertClicked();
    }

    private void populateAlerts() {

        titles = new ArrayList<String>();

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

    private void alertClicked() {
        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                for(Alert alert: alerts){
                    if(alert.getTitle().equals(textView.getText().toString())){
                        sendReport(alert);
                        break;
                    }
                }
            }
        });
    }

    private void sendReport(Alert alert) {
        // Start using Spring
        String url = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/report";

        // Get Android school
        School school = new School();
        school.setId(4);

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
        try{
            String response = new CreateReportsTask().execute(report).get();
            Log.wtf(TAG, response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }
}

