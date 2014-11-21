//package com.ordonezalex.bebrave.tasks;
//
//import android.os.AsyncTask;
//import android.os.Message;
//import android.util.Log;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ordonezalex.bebrave.util.User;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//public class CreateUserTask extends AsyncTask<User, Void, String> {
//    public final static String TAG = "BeBrave";
//    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/User";
//
//    @Override
//    protected String doInBackground(User... users) {
//
//        User user;
//        uesr = users[0];
//        Message message = new Message();
//
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(new MediaType("application", "json"));
//        HttpEntity<User> requestEntity = new HttpEntity<User>(users, requestHeaders);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//        String result = null;
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
//            result = response.getBody();
//        } catch (HttpServerErr orException e) {
//            Log.wtf(TAG, e.getResponseBodyAsString());
//        } catch (HttpClientErrorException e) {
//            Log.wtf(TAG, e);
//        }
//
//        return result;
//    }
//}
