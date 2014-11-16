package com.ordonezalex.bebrave.tasks;

import android.os.AsyncTask;

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
}
