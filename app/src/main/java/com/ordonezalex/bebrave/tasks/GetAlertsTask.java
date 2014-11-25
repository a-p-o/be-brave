package com.ordonezalex.bebrave.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ArrayAdapter;

import com.ordonezalex.bebrave.AlertActivity;
import com.ordonezalex.bebrave.R;
import com.ordonezalex.bebrave.util.Alert;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class GetAlertsTask extends AsyncTask<Void, Void, Alert[]> {
    public final static String TAG = "BeBrave";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/Alert";
    private AlertActivity activity;
    public final static int NOTIFICATION_REPORT_SENDING_ID = 2;
    private Notification.Builder builder;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public GetAlertsTask(AlertActivity activity) {

        super();
        this.setActivity(activity);
        this.builder = new Notification.Builder(activity)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(activity.getResources().getString(R.string.notification_share_walk_title))
                .setContentText(activity.getResources().getString(R.string.notification_share_walk_text))
                .setOngoing(true)
                .setPriority(NOTIFICATION_REPORT_SENDING_ID);
    }

    @Override
    protected Alert[] doInBackground(Void... voids) {
        // Set the Accept header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson message converter
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // Make the HTTP GET request, marshaling the response from JSON to an array of Events
        ResponseEntity<Alert[]> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, Alert[].class);
        Alert[] alert = responseEntity.getBody();

        return alert;
    }

    @Override
    protected void onPostExecute(Alert[] alerts) {

        for (Alert alert : alerts) {
            if (alert.getSchool().getId() == this.activity.userSchoolId) {
                this.activity.titles.add(alert.getTitle());
            }
        }

        // Add Alerts to adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, this.activity.titles);
        this.activity.alertListView.setAdapter(adapter);
        super.onPostExecute(alerts);
    }

    public void setActivity(AlertActivity activity) {
        this.activity = activity;
    }
}
