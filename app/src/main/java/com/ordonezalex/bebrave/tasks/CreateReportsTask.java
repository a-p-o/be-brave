package com.ordonezalex.bebrave.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ordonezalex.bebrave.util.Report;

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
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        report = reports[0];

        restTemplate.getMessageConverters();

        String result = null;

        try {
            result = restTemplate.postForObject(URL, report, String.class);
        } catch (HttpServerErrorException e) {
            Log.wtf(TAG, e);
        } catch (HttpClientErrorException e) {
            Log.wtf(TAG, e);
        }

        return result;
    }
}
