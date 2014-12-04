package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public  List<String> titles;
    public ListView alertListView;
    private Alert[] alerts;
    public static long userSchoolId = 2; // Will be pulled from user
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
        new GetAlertsTask(this).execute();

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
        school.setId(3);

        // Get Help me status
        Status status = new Status();
        status.setId(2);

        // Get textUser User
        User user = new User();
        user.setId(2);

        Report report = new Report();
        report.setUser(user);
        report.setAlert(alert);
        report.setStatus(status);
        report.setSchool(school);

        ObjectMapper mapper = new ObjectMapper();
        try{
            Log.i(TAG, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report) );
            String response = new CreateReportsTask(this).execute(report).get();
            Log.wtf(TAG, response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void setAlerts(Alert[] alerts) {
        this.alerts = alerts;
    }
}

