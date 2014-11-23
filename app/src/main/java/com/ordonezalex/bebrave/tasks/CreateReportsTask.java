package com.ordonezalex.bebrave.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ordonezalex.bebrave.util.Report;

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
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/Report";

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

    @Override
    protected void onPostExecute(String response) {

        Log.i(TAG, "Reponse: " + response);

        super.onPostExecute(response);
    }
}
