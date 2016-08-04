package com.fame.plumbum.chataround;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.adapters.Comments_adapter;
import com.fame.plumbum.chataround.chat.SelfChatList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pankaj on 23/7/16.
 */
public class ParticularPost extends AppCompatActivity implements View.OnClickListener, LocationListener {
    EditText add_comment;
    TextView post_title;
    ImageButton report, chat_button;
    String post_id = "", uid = "", poster_id = "", user_name="";
    private static JSONObject postDetails;
    private static Comments_adapter ca;
    private LocationManager locationManager;
    private String provider, poster_name;
    boolean isGPSEnabled=false, isNetworkEnabled=false, doneSomething = false, sendingComment = false;
    double lat, lng;
    private JSONArray comments;
    int len = 1;
    ListView comments_list;
    TextView report_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_post);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                if (add_comment.getText().toString().length()>0){
                    sendingComment = true;
                    getLocation();
                }
                break;
            case R.id.chat_button:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                if (!poster_id.contentEquals(sp.getString("uid", null))) {
                    final Intent intent = new Intent(ParticularPost.this, ParticularChat.class);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("uid_r", poster_id);
                    intent.putExtra("poster_name", poster_name);
                    intent.putExtra("remote_name", poster_name);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ParticularPost.this, SelfChatList.class);
                    intent.putExtra("post_id", post_id);
                    startActivity(intent);
                }
                break;
            case R.id.like_button:
                RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                        getRequestQueue();
                getLocation();
                Log.e("like", "http://52.66.45.251:8080/Like?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name+"&Latitude="+lat+"&Longitude="+lng);
                StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/Like?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20")+"&Latitude="+lat+"&Longitude="+lng,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.e("like_reponse", response);
                                    JSONObject jO = new JSONObject(response);
                                    if (jO.getString("Status").contentEquals("200")){
                                        refresh();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParticularPost.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                        Log.getStackTraceString(error);
                    }
                });
                MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
                break;
        }
    }

    private void sendComment(String s) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/Comment?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20")+"&Comment="+s.replace(" ", "%20")+"&Latitude="+lat+"&Longitude="+lng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Comment_response", response);
                        if (response.contains("not in range")){
                            Toast.makeText(ParticularPost.this, "You are not in range", Toast.LENGTH_SHORT).show();
                        }else {
                            refresh();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularPost.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                Log.getStackTraceString(error);
            }
        });
        MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
    }

    private void refresh() {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        Log.e("TAG_PARTICULAR_POST", "http://52.66.45.251:8080/GetPostDetailed?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/GetPostDetailed?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("PARTICULARPOST", response);
                            JSONObject jO = new JSONObject(response);
                            getDetails(jO);
                            update();
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularPost.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
    }

    private void update() {
        try {
            TextView midText = (TextView)findViewById(R.id.midText);
            post_id = postDetails.getString("PostId");
            poster_id = postDetails.getString("PosterId");
            poster_name = postDetails.getString("PosterName");
            if (post_id.contentEquals(uid)){
                chat_button.setVisibility(View.GONE);
            }
            post_title.setText(postDetails.getString("Post"));
            if (!postDetails.getString("NoOfLikes").contentEquals("0")){
                if (postDetails.getString("NoOfLikes").contentEquals("1")){
                    post_title.setBackgroundColor(0xffd42f2f);
                    report_count.setText("1 person reported this.");
                }
                else{
                    post_title.setBackgroundColor(0xffd42f2f);
                    report_count.setText(postDetails.getString("NoOfLikes") + " people reported this.");
                }
            }else{
                post_title.setBackgroundColor(0xffDfFDF4);
                report_count.setText("No one reported this.");
            }
            if (ca == null) {
                if (postDetails.getJSONArray("Comments").length()>0) {
                    Log.e("CA", "NULL_GREATEER");
                    comments_list.setVisibility(View.VISIBLE);
                    midText.setVisibility(View.GONE);
                    ca = new Comments_adapter(ParticularPost.this, postDetails.getJSONArray("Comments"));
                    Log.e("LENGTH", ca.total+ " ");
                    comments_list.setAdapter(ca);
                }
                else {
                    len = 0;
                    Log.e("CA", "NULL_ZERO");
                    comments_list.setVisibility(View.GONE);
                    midText.setVisibility(View.VISIBLE);
                }
            }else{
                if (postDetails.getJSONArray("Comments").length()>0) {
                    Log.e("CA", "NOT_NULL_GREATEER");
                    comments_list.setVisibility(View.VISIBLE);
                    midText.setVisibility(View.GONE);
                    ca = new Comments_adapter(ParticularPost.this, postDetails.getJSONArray("Comments"));
                    comments_list.setAdapter(ca);
                }else{
                    Log.e("CA", "NOT_NULL_ZERO");
                    len = 0;
                    comments_list.setVisibility(View.GONE);
                    midText.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            Log.getStackTraceString(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

    public static void getDetails(JSONObject post) {
        postDetails = post;
    }

    private void init(){
        post_id = getIntent().getExtras().getString("post_id");
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        comments_list = (ListView) findViewById(R.id.list_comments_post);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedPreferences.getString("uid", null);
        user_name = sharedPreferences.getString("user_name", null);
        if (user_name == null){
            user_name = "";
        }
        add_comment = (EditText) findViewById(R.id.comment_add);
        post_title = (TextView) findViewById(R.id.post_title);
        ImageButton sendComment = (ImageButton) findViewById(R.id.add_button);
        report = (ImageButton) findViewById(R.id.like_button);
        report_count = (TextView) findViewById(R.id.reports_count);
        chat_button = (ImageButton) findViewById(R.id.chat_button);
        report_count = (TextView) findViewById(R.id.reports_count);
        sendComment.setOnClickListener(this);
        report.setOnClickListener(this);
        chat_button.setOnClickListener(this);
        refresh();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG_LOCATION", "NO LOCATION");
            return;
        }
        doneSomething = false;
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (!isGPSEnabled() && !isWIFIEnabled())
            Toast.makeText(this, "Please turn on GPS!", Toast.LENGTH_SHORT).show();
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            doneSomething = true;
            if (sendingComment) {
                sendComment(add_comment.getText().toString().replace(" ", "%20"));
                add_comment.setText("");
            }
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        }
    }

    public boolean isGPSEnabled() {
        try {
            if (locationManager == null)
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.e("Error", "GPS");
            //toast("Exception " + e.toString());
        }
        return isGPSEnabled;
    }

    public boolean isWIFIEnabled() {
        try {
            if (locationManager == null)
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e("Error", "WiFi");
//            toast("Exception " + e.toString());
        }
        return isNetworkEnabled;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (sendingComment && !doneSomething) {
            doneSomething = true;
            lat = Math.round(location.getLatitude() * 100) / 100;
            lng = Math.round(location.getLongitude() * 100) / 100;
            sendComment(add_comment.getText().toString().replace(" ", "%20"));
            add_comment.setText("");
            sendingComment = false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
