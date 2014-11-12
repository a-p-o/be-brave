package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class AlertActivity extends Activity {

    private ListView alertListView;
    private HttpClient client;
    private JSONObject json;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/alert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize the text views that we are going to work with

        //set up the http client for rest requests
        client = new DefaultHttpClient();
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("ID");
        fields.add("School");
        fields.add("Title");
        fields.add("Color");
        fields.add("Priority");
        fields.add("Enabled");
        new Read().execute(fields);

    }
    
    public JSONObject alertInformation(String username) throws ClientProtocolException, IOException, JSONException
    {
        StringBuilder url = new StringBuilder(URL);

        HttpGet get = new HttpGet(url.toString());
        get.setHeader("Content-Type" , "application/json");
        HttpResponse response = client.execute(get);
        int status = response.getStatusLine().getStatusCode();

        if ( status == 200)
        {
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            JSONArray alertData = new JSONArray(data);
            JSONObject alert = alertData.getJSONObject(0);
            return alert;
        }
        else
        {
            Toast.makeText(this, status, Toast.LENGTH_LONG);
            return null;
        }
    }

    public class Read extends AsyncTask<ArrayList<String>,Integer , ArrayList<String>>
    {
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            for(int i =0; i < result.size();i++)
            {
                alertListView.addFooterView(alertListView, result.get(i), true);
            }
            super.onPostExecute(result);
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {
            try {
                json = alertInformation("user");
                ArrayList<String> results = new ArrayList<String>();
                results.add(json.getString(params[0].get(0)));
                results.add(json.getString(params[0].get(1)));
                return results;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

