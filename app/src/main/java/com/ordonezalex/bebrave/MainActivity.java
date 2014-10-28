package com.ordonezalex.bebrave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivity extends Activity {
    public final static String TAG = "BeBrave";
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v0/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button alertButton = (Button) findViewById(R.id.alert_button);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAlert();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

//        menu.findItem(R.id.action_profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                return true;
//            }
//        });

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
        } else if (id == R.id.action_profile) {
            openProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void createAlert() {

        // Create client
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL + "/alert");

        try {
            // Add parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("SchoolId", "0"));
            parameters.add(new BasicNameValuePair("Color", "50"));
            parameters.add(new BasicNameValuePair("Enabled", "true"));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
//            httpPost.addHeader("Accept", "application/json");

            // Execute request
            httpClient.execute(httpPost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block

            Toast.makeText(this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block

            Toast.makeText(this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        }
    }
}

