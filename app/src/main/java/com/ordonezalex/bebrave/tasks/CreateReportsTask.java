package com.ordonezalex.bebrave.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

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

    public static final String TAG = "BeBrave";
//    private final static String URL = App.getContext().getString(R.string.api_target_base_uri)
//            + "/"
//            + App.getContext().getString(R.string.api_target_version)
//            + "/Report";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v2/Report";
    private static final String EXTRA_REPORT_ID = "EXTRA_REPORT_ID";

    private Activity activity;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CreateReportsTask(Activity activity) {

        super();
        this.setActivity(activity);
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.activity);
            Log.i(TAG, "The Logged in user is: "+ prefs.getString("Username", "no Username"));

            startLocationServiceIntent.putExtra(EXTRA_REPORT_ID, result.getInt("ID"));

            Log.i(TAG, "Created location service.");
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e);
        }

        activity.startService(startLocationServiceIntent);

        super.onPostExecute(response);
    }

    public void setActivity(Activity activity) {

        this.activity = activity;
    }
}
