package com.ordonezalex.bebrave.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ordonezalex.bebrave.AlertActivity;
import com.ordonezalex.bebrave.LoginActivity;
import com.ordonezalex.bebrave.R;
import com.ordonezalex.bebrave.SignUpActivity;
import com.ordonezalex.bebrave.util.Alert;
import com.ordonezalex.bebrave.util.Login;
import com.ordonezalex.bebrave.util.Report;
import com.ordonezalex.bebrave.util.User;

import org.json.JSONArray;
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


public class LoginTask extends AsyncTask<Login, Void, String> {
    public final static String TAG = "BeBrave";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v2/Login";
    private LoginActivity activity;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public LoginTask(LoginActivity activity) {

        super();
        this.setActivity(activity);

    }

    public void setActivity(LoginActivity activity) {
        this.activity = activity;

    }

    @Override
    protected String doInBackground(Login... logins) {
        Login login;
        login = logins[0];

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json"));
        HttpEntity<Login> requestEntity = new HttpEntity<Login>(login, requestHeaders);

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
    protected void onPostExecute(String s) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = prefs.edit();
        try {

            JSONObject user = new JSONObject(s);
            editor.putString("Username" , user.getString("Username"));
            Log.i(TAG, "Added username: "+user.getString("Username"));
            editor.putString("SecretToken", user.getString("ClientSecretToken"));
            Log.i(TAG, "Added secret Token: " + user.getString("ClientSecretToken"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(s);
    }
}