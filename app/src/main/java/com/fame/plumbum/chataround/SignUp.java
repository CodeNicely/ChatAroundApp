package com.fame.plumbum.chataround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

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
                    else {
                        registerUser();
                    }
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

    private void sendData(String email, String password, String s) {
        this.email = email;
        this.password = password;
        loginFlag = s;
        registerUser();
    }

    void registerUser(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/CreateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            Log.e("REPONS_SIGNUP", response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                editor.apply();
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(SignUp.this, "Email  already registered", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.getStackTraceString(error);
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

    private void sendFCM(String uid){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains("token")) {
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                    getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/GetFCMToken/UserId="+uid+"&Token="+ sp.getString("token", null),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUp.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                    Log.getStackTraceString(error);
                }
            });
            MySingleton.getInstance(SignUp.this).addToRequestQueue(stringRequest);
        }
        else {
            Log.e("FIREBIRDTOKEN", FirebaseInstanceId.getInstance().getToken());
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                    getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/Post?UserId="+uid+"&Token="+FirebaseInstanceId.getInstance().getToken(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(SignUp.this, "Posted", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUp.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                    Log.getStackTraceString(error);
                }
            });
            MySingleton.getInstance(SignUp.this).addToRequestQueue(stringRequest);
        }
    }
}
