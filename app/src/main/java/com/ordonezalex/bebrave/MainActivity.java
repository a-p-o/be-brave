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

import com.ordonezalex.bebrave.tasks.CreateAlertsTask;
import com.ordonezalex.bebrave.util.Alert;

import org.apache.http.HttpResponse;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    public final static String TAG = "BeBrave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button alertButton = (Button) findViewById(R.id.alert_button);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpResponse httpResponse;

                try {
                    httpResponse = new CreateAlertsTask().execute(new Alert("0", "60", true)).get();
                    // See http://tools.ietf.org/html/rfc7231#section-6
                    if (httpResponse.getStatusLine().getStatusCode() < 300 && httpResponse.getStatusLine().getStatusCode() >= 200) {

                        Toast.makeText(MainActivity.this, R.string.alert_created, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Status code: " + httpResponse.getStatusLine().getStatusCode());
                    }
                } catch (InterruptedException e) {
                    Toast.makeText(MainActivity.this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.toString());
                } catch (ExecutionException e) {
                    Toast.makeText(MainActivity.this, R.string.alert_created_error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.toString());
                }
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
}

