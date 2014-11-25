package com.ordonezalex.bebrave;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button changeProfilePicButton;
    private ImageView profileImage;
    private GestureDetector gestureDetector;
    private HttpClient client;
    private JSONObject json;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v0/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gestureDetector = new GestureDetector(this,
                new SwipeGestureDetector());

        profileImage = (ImageView) findViewById(R.id.profile_picture_imageview);

        //initialize the text views that we are going to work with
        usernameTextView = (TextView) findViewById(R.id.user_name_text_view);
        userEmailTextView = (TextView) findViewById(R.id.user_email_text_view);
        userIdTextView = (TextView) findViewById(R.id.user_id_text_view);

        changeProfilePicButton = (Button) findViewById(R.id.change_picture_button);
        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        //set up the http client for rest requests
        client = new DefaultHttpClient();
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("Username");
        fields.add("OrganizationIdentifier");
        fields.add("FName");
        fields.add("LName");
        new Read().execute(fields);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onLeftSwipe() {
        // Open profile when swiped left
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle translateBundle = ActivityOptions.makeCustomAnimation(this, R.anim.activity_left_open_translate, R.anim.activity_left_close_translate).toBundle();
        this.startActivity(intent , translateBundle);
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


            String name = result.get(2)+ ", " + result.get(3);
            usernameTextView.setText(name);
            userIdTextView.setText(result.get(1));
            userEmailTextView.setText(result.get(0));

            super.onPostExecute(result);
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {
            try {
                json = profileInformation("user");
                ArrayList<String> results = new ArrayList<String>();
                results.add(json.getString(params[0].get(0)));
                results.add(json.getString(params[0].get(1)));
                results.add(json.getString(params[0].get(2)));
                results.add(json.getString(params[0].get(3)));

                return results;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    ProfileActivity.this.onLeftSwipe();


                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}
