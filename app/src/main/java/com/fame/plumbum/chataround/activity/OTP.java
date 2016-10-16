package com.fame.plumbum.chataround.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pankaj on 19/8/16.
 */
public class OTP extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        final EditText edt = (EditText)findViewById(R.id.edit_otp);
        Button btn = (Button)findViewById(R.id.okay_otp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString().length()!=0){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OTP.this);
                    sendOTP(edt.getText().toString(), sp.getString("uid", ""));
                }else
                    Toast.makeText(OTP.this, "Please enter correct code!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String s, String uid) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/NumberVerify?UserId=" + uid + "&OTP="+s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jO;
                        try {
                            jO = new JSONObject(response);
                        if (jO.getString("Status").contentEquals("200")) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OTP.this);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("otp_verified", "1");
                            editor.apply();
                            Intent intent = new Intent(OTP.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTP.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(OTP.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}