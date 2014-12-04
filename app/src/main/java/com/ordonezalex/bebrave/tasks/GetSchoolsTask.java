//package com.ordonezalex.bebrave.tasks;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.Notification;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.widget.ArrayAdapter;
//
//
//import com.ordonezalex.bebrave.SignUpActivity;
//import com.ordonezalex.bebrave.R;
//import com.ordonezalex.bebrave.util.School;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//
//public class GetSchoolsTask extends AsyncTask<Void, Void, School[]> {
//    public final static String TAG = "BeBrave";
//    private final static String URL = "http://caffeinatedcm-001-site3.smarterasp.net/api/v1/School";
//    private SignUpActivity activity;
//    public final static int NOTIFICATION_REPORT_SENDING_ID = 2;
//    private Notification.Builder builder;
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public GetSchoolsTask(SignUpActivity activity) {
//
//        super();
//        this.setActivity(activity);
//        this.builder = new Notification.Builder(activity)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(activity.getResources().getString(R.string.notification_share_walk_title))
//                .setContentText(activity.getResources().getString(R.string.notification_share_walk_text))
//                .setOngoing(true)
//                .setPriority(NOTIFICATION_REPORT_SENDING_ID);
//    }
//
//    @Override
//    protected School[] doInBackground(Void... voids) {
//        // Set the Accept header
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
//        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
//
//        // Create a new RestTemplate instance
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Add the Jackson message converter
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//        // Make the HTTP GET request, marshaling the response from JSON to an array of Events
//        ResponseEntity<School[]> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, School[].class);
//        School[] School = responseEntity.getBody();
//
//        return School;
//    }
//
//    @Override
//    protected void onPostExecute(School[] Schools) {
//
//        for (School School : Schools) {
//            if (School.getSchool().getId() == this.activity.userSchoolId) {
//                this.activity.titles.add(School.getTitle());
//            }
//        }
//
//        // Add Schools to adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, this.activity.titles);
//        this.activity.SchoolListView.setAdapter(adapter);
//        super.onPostExecute(Schools);
//    }
//
//    public void setActivity(SignUpActivity activity) {
//        this.activity = activity;
//    }
//}
