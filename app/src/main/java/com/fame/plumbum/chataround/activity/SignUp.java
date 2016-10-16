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
import com.android.volley.toolbox.Volley;
import com.fame.plumbum.chataround.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 11/7/16.
 */
public class SignUp extends AppCompatActivity{
    EditText pass_edit, email_edit;
    String password, email, loginFlag = "0";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        if (checkPlayServices()) {
            pass_edit = (EditText) findViewById(R.id.pass_edit);
            email_edit = (EditText) findViewById(R.id.email_edit);
            Button button = (Button) findViewById(R.id.sign_up);
            Button signin = (Button) findViewById(R.id.sign_in);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = pass_edit.getText().toString();
                    email = email_edit.getText().toString();
                    if (email.length() < 5)
                        Toast.makeText(SignUp.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                    else if (password.length() < 5)
                        Toast.makeText(SignUp.this, "Password too short", Toast.LENGTH_SHORT).show();
                    else if (email.indexOf("@")>1 && email.indexOf(".", email.indexOf("@"))>email.indexOf("@") && email.indexOf("@", email.indexOf("@"))>0){
                        registerUser();
                    }else
                        Toast.makeText(SignUp.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                }
            });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignUp.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    void registerUser(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/CreateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                editor.apply();
                                Intent intent = new Intent(SignUp.this, GetProfileDetails.class);
                                startActivity(intent);
                                finish();
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(SignUp.this, "Email  already registered", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ignored) {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email.replace(" ", "%20"));
                params.put("Password", password.replace(" ", "%20"));
                params.put("LoginFlag", loginFlag);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
