package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ordonezalex.bebrave.tasks.GetAlertsTask;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.School;
import com.ordonezalex.bebrave.util.Status;
import com.ordonezalex.bebrave.util.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class AlertActivity extends Activity {

    private ListView alertListView;
    private Alert[] alerts;
    private String[] titles;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/alert";
    private long userSchool = 4;//will be pulled from user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        alertListView = (ListView) findViewById(R.id.alerts_listView);
        populateAlerts();

    }

    private void populateAlerts() {
        alerts = new Alert[0];

        try {
            alerts = new GetAlertsTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        titles = new String[alerts.length];
        int count = 0;

        //Filter out alerts from other schools
        for(int i = 0; i < alerts.length; i++){
            if(alerts[i].getSchool().getId() == userSchool){
                titles[count] = alerts[i].getTitle();
                count ++;
            }
        }

        //add alerts to adapter
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_alerts, titles);
        alertListView.setAdapter(adapter);
    }
}

