package com.fame.plumbum.chataround.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Comments_adapter;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.helper.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 23/7/16.
 */
public class ParticularPost extends AppCompatActivity implements View.OnClickListener{
    EditText add_comment;
    TextView post_title, poster_name_txt;
    ImageButton report, chat_button;
    CircleImageView image_user;
    String post_id = "", uid = "", poster_id = "", user_name="";
    private static JSONObject postDetails;
    private static Comments_adapter ca;
    private String poster_name;
    double lat, lng;
    int len = 1;
    ListView comments_list;
    TextView report_count;
    RelativeLayout rl_progress;
    BroadcastReceiver receiver = null;

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
                    rl_progress.setVisibility(View.VISIBLE);
                    getLocation();
                }
                break;
            case R.id.chat_button:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                if (!poster_id.contentEquals(sp.getString("uid", null))) {
                    final Intent intent = new Intent(ParticularPost.this, ParticularChat.class);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("uid_r", poster_id);
                    intent.putExtra("poster_name", poster_name.replace("%20", " "));
                    intent.putExtra("remote_name", poster_name.replace("%20", " "));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ParticularPost.this, SelfChatList.class);
                    intent.putExtra("post_id", post_id);
                    startActivity(intent);
                }
                break;
            case R.id.like_button:
                MySingleton.getInstance(getApplicationContext()).
                        getRequestQueue();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL+"Like?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20")+"&Latitude="+lat+"&Longitude="+lng,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
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
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
                break;
        }
    }

    private void sendComment(String s) {
        MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251/Comment?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20")+"&Comment="+s.replace(" ", "%20")+"&Latitude="+lat+"&Longitude="+lng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rl_progress.setVisibility(View.GONE);
                        if (response.contains("not in range")){
                            Toast.makeText(ParticularPost.this, "You are not in range", Toast.LENGTH_SHORT).show();
                        }else {
                            refresh();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rl_progress.setVisibility(View.GONE);
                Toast.makeText(ParticularPost.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
    }

    private void refresh() {
        MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251/GetPostDetailed?UserId="+uid+"&PostId="+post_id+"&UserName="+user_name.replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            getDetails(jO);
                            update();
                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularPost.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
    }

    private void update() {
        try {
            TextView midText = (TextView)findViewById(R.id.midText);
            post_id = postDetails.getString("PostId");
            poster_id = postDetails.getString("PosterId");
            poster_name = postDetails.getString("PosterName");
            poster_name_txt.setText(poster_name);
            getImage(poster_id, image_user);
            if (post_id.contentEquals(uid)){
                chat_button.setVisibility(View.GONE);
            }
            post_title.setText(postDetails.getString("Post"));
            if (!postDetails.getString("NoOfLikes").contentEquals("0")){
                if (postDetails.getString("NoOfLikes").contentEquals("1")){
                    report_count.setText("1 person reported this.");
                }
                else{
                    report_count.setText(postDetails.getString("NoOfLikes") + " people reported this.");
                }
            }else{
                report_count.setText("No one reported this.");
            }
            DBHandler db = new DBHandler(this);
            if (db.getPeronalChats(post_id).size()>0){

            }
            db.close();
            if (postDetails.getString("LikeFlag").contentEquals("0"))
                report.setBackgroundResource(R.drawable.thumbs_down_accent);
            else
                report.setBackgroundResource(R.drawable.thumbs_down_red);
            if (ca == null) {
                if (postDetails.getJSONArray("Comments").length()>0) {
                    comments_list.setVisibility(View.VISIBLE);
                    midText.setVisibility(View.GONE);
                    ca = new Comments_adapter(ParticularPost.this, postDetails.getJSONArray("Comments"));
                    comments_list.setAdapter(ca);
                }
                else {
                    len = 0;
                    comments_list.setVisibility(View.GONE);
                    midText.setVisibility(View.VISIBLE);
                }
            }else{
                if (postDetails.getJSONArray("Comments").length()>0) {
                    comments_list.setVisibility(View.VISIBLE);
                    midText.setVisibility(View.GONE);
                    ca = new Comments_adapter(ParticularPost.this, postDetails.getJSONArray("Comments"));
                    comments_list.setAdapter(ca);
                }else{
                    len = 0;
                    comments_list.setVisibility(View.GONE);
                    midText.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException ignored) {
        }
    }

    private String getImage(String uid, final CircleImageView img_user) {
        final String[] image_name = new String[1];
        MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/ImageName?UserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            image_name[0] = json.getString("ImageName");
                            picassoLoad(image_name[0], img_user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularPost.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ParticularPost.this).addToRequestQueue(stringRequest);
        return image_name[0];
    }

    private void picassoLoad(String s, CircleImageView img_user) {
        Picasso.with(this).load("http://52.66.45.251/ImageReturn?ImageName="+s).error(R.drawable.user).into(img_user);
    }

    public static void getDetails(JSONObject post) {
        postDetails = post;
    }

    private void init(){
        post_id = getIntent().getExtras().getString("post_id");
        rl_progress = (RelativeLayout) findViewById(R.id.rl_progress);
        comments_list = (ListView) findViewById(R.id.list_comments_post);
        image_user = (CircleImageView) findViewById(R.id.image_user);
        poster_name_txt = (TextView) findViewById(R.id.poster_name);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedPreferences.getString("uid", "");
        user_name = sharedPreferences.getString("user_name", "");
        add_comment = (EditText) findViewById(R.id.comment_add);
        post_title = (TextView) findViewById(R.id.post_title);
        ImageView sendComment = (ImageView) findViewById(R.id.add_button);
        report = (ImageButton) findViewById(R.id.like_button);
        report_count = (TextView) findViewById(R.id.reports_count);
        chat_button = (ImageButton) findViewById(R.id.chat_button);
        report_count = (TextView) findViewById(R.id.reports_count);
        sendComment.setOnClickListener(this);
        report.setOnClickListener(this);
        chat_button.setOnClickListener(this);
        if (receiver==null) {
            IntentFilter filter = new IntentFilter("Hello World");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().contentEquals("Hello World")) {
                        lat = intent.getDoubleExtra("lat", 0.0);
                        lng = intent.getDoubleExtra("lng", 0.0);
                    }
                }
            };
            registerReceiver(receiver, filter);
        }
        refresh();
    }

    private void getLocation() {

            sendComment(add_comment.getText().toString().replace(" ", "%20"));
            add_comment.setText("");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver==null) {
            IntentFilter filter = new IntentFilter("Hello World");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().contentEquals("Hello World")) {
                        lat = intent.getDoubleExtra("lat", 0.0);
                        lng = intent.getDoubleExtra("lng", 0.0);
                    }
                }
            };
            registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}