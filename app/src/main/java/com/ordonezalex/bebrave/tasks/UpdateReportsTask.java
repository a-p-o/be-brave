package com.ordonezalex.bebrave.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordonezalex.bebrave.App;
import com.ordonezalex.bebrave.MainActivity;
import com.ordonezalex.bebrave.util.Location;

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

public class UpdateReportsTask extends AsyncTask<Location, Void, String> {
    public final static String TAG = "BeBrave";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v2/Report";
    private Context context;
    private int reportId;

    public UpdateReportsTask(int reportId, Context cxt) {

        super();
        this.setReportId(reportId);
        context = cxt;
    }

    @Override
    protected String doInBackground(Location... locations) {

        Location location;
        location = locations[0];

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json"));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i(TAG, "the username is: " + prefs.getString("Username", "no username"));
        HttpEntity<Location> requestEntity = new HttpEntity<Location>(location, requestHeaders);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        ObjectMapper mapper = new ObjectMapper();

        try {
            Log.i(TAG, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(location));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String result;
        String url = URL + "/" + reportId + "/Location";

        try {
            Log.i(TAG, "Updating location to: " + url);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            result = response.getBody();

            return result;
        } catch (HttpServerErrorException e) {
            Log.wtf(TAG, e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            Log.wtf(TAG, e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {

        Log.i(TAG, "Response: " + response);

        super.onPostExecute(response);
    }

    public void setReportId(int reportId) {

        this.reportId = reportId;
    }
}
