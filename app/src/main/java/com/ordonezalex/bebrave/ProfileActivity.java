package com.ordonezalex.bebrave;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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


public class ProfileActivity extends Activity {

    private TextView usernameTextView;
    private TextView userEmailTextView;
    private TextView userIdTextView;
    private HttpClient client;
    private JSONObject json;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v0/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize the text views that we are going to work with
        usernameTextView = (TextView) findViewById(R.id.user_name_text_view);
        userEmailTextView = (TextView) findViewById(R.id.user_email_text_view);
        userIdTextView = (TextView) findViewById(R.id.user_id_text_view);

        //set up the http client for rest requests
        client = new DefaultHttpClient();
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("Username");
        fields.add("OrganizationIdentifier");
        new Read().execute(fields);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public JSONObject profileInformation(String username) throws ClientProtocolException, IOException, JSONException
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
            JSONArray profiledata = new JSONArray(data);
            JSONObject profile = profiledata.getJSONObject(0);
            return profile;
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

            usernameTextView.setText(result.get(0));
            userIdTextView.setText((result.get(1)));
            super.onPostExecute(result);
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {
            try {
                json = profileInformation("user");
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
