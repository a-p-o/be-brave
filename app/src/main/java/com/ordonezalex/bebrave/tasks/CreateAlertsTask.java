package com.ordonezalex.bebrave.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ordonezalex.bebrave.util.Alert;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAlertsTask extends AsyncTask<Alert, Void, HttpResponse> {
    public final static String TAG = "BeBrave";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v0";

    @Override
    protected HttpResponse doInBackground(Alert... alerts) {

        Alert alert;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpResponse;

        // Get first Alert from arguments
        alert = alerts[0];

        // Create client
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(URL + "/alert");

        try {
            // Add parameters
            List<NameValuePair> httpParameters = new ArrayList<NameValuePair>();
            httpParameters.add(new BasicNameValuePair("SchoolId", alert.getSchoolId()));
            httpParameters.add(new BasicNameValuePair("Color", alert.getColor()));
            httpParameters.add(new BasicNameValuePair("Enabled", "" + alert.isEnabled()));
            httpPost.setEntity(new UrlEncodedFormEntity(httpParameters));

            // Execute request
            httpResponse = httpClient.execute(httpPost);

            return httpResponse;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block

//            Toast.makeText(this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block

//            Toast.makeText(this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }
}
