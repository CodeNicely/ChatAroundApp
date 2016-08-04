package com.fame.plumbum.chataround;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 26/7/16.
 */
public class UserProfile extends AppCompatActivity {

    TextView phone_text, name_text;
    CircleImageView user;
    String uid="";
//    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone_text = (TextView) findViewById(R.id.phone_text);
        name_text = (TextView) findViewById(R.id.name_text);
        user = (CircleImageView) findViewById(R.id.image_user);
        uid = getIntent().getExtras().getString("uid");
        getImage();
        receiveData();
    }

    private void getImage(){
        Picasso.with(UserProfile.this).load("http://52.66.45.251:8080/ImageReturn?UserId=" + uid).resize(512, 512).into(user);
    }


    private void receiveData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                name_text.setText(jO.getString("Name"));
                                phone_text.setText(jO.getString("Mobile"));
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(UserProfile.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR_VOLLETY", error.toString());
                        Toast.makeText(UserProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", uid);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }
}
