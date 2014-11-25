package com.ordonezalex.bebrave.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.ordonezalex.bebrave.MainActivity;
import com.ordonezalex.bebrave.R;
import com.ordonezalex.bebrave.services.LocationService;
import com.ordonezalex.bebrave.util.Report;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class CreateReportsTask extends AsyncTask<Report, Void, String> {
    public final static String TAG = "BeBrave";
    public final static int NOTIFICATION_REPORT_SENDING_ID = 2;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/Report";
    private static final String EXTRA_REPORT_ID = "EXTRA_REPORT_ID";

    private Activity activity;

    private Notification.Builder builder;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CreateReportsTask(Activity activity) {

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
    protected String doInBackground(Report... reports) {

        Report report;
        report = reports[0];

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json"));
        HttpEntity<Report> requestEntity = new HttpEntity<Report>(report, requestHeaders);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        String result;

        try {
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
            result = response.getBody();
            return result;
        } catch (HttpServerErrorException e) {
            Log.wtf(TAG, e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            Log.wtf(TAG, e);
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(String response) {

        Log.i(TAG, "Response: " + response);

        Intent startLocationServiceIntent = new Intent(activity, LocationService.class);

        try {
            JSONObject result = new JSONObject(response);

            Log.i(TAG, "Creating location service with report ID " + result.getInt("ID"));

            startLocationServiceIntent.putExtra(EXTRA_REPORT_ID, result.getInt("ID"));

            Log.i(TAG, "Created location service.");
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e);
        }

        activity.startService(startLocationServiceIntent);

        // Show persistent notification
        Intent resultIntent = new Intent(activity, activity.getClass());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_REPORT_SENDING_ID, builder.build());

        super.onPostExecute(response);
    }

    public void setActivity(Activity activity) {

        this.activity = activity;
    }
}
