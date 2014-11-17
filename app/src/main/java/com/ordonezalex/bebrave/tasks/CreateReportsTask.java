package com.ordonezalex.bebrave.tasks;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        Message message = new Message();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json"));
        HttpEntity<Report> requestEntity = new HttpEntity<Report>(report, requestHeaders);

        ObjectMapper mapper = new ObjectMapper();

//        try {
//            Log.i(TAG, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        String result = null;

        try {
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
            result = response.getBody();
        } catch (HttpServerErrorException e) {
            Log.wtf(TAG, e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            Log.wtf(TAG, e);
        }

        return result;
    }
}
