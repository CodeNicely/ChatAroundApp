package com.fame.plumbum.chataround;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fame.plumbum.chataround.chat.MainWindow;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText pass_edit, email_edit;
    LoginButton fb_signin;
    CallbackManager callbackManager;
    private static final String TAG = MainActivity.class.getSimpleName();
    String password, email, loginFlag = "0";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_login);
        pass_edit = (EditText) findViewById(R.id.pass_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        Button button = (Button) findViewById(R.id.sign_up);
        Button login = (Button) findViewById(R.id.login);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains("uid")){
            Intent intent = new Intent(this, MainWindow.class);
            startActivity(intent);
            finish();
        }
        if (!sp.contains("token")){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token", FirebaseInstanceId.getInstance().getToken());
            editor.apply();
        }

        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = pass_edit.getText().toString();
                    email = email_edit.getText().toString();
                    if (email.length() < 5)
                        Toast.makeText(LoginActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                    else if (password.length() < 5)
                        Toast.makeText(LoginActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                    else {
                        registerUser();
                    }
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.GET_ACCOUNTS,
            }, 10);
        }
        fb_signin = (LoginButton) findViewById(R.id.fb_signin);
        fb_signin.setLoginBehavior(LoginBehavior.WEB_ONLY);
        fb_signin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        fb_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "fb");
                getLoginDetails(fb_signin);
            }
        });
    }

    private void sendData(String email, String password, String s) {
        this.email = email;
        this.password = password;
        loginFlag = s;
        registerUserWithFb();
    }

    void registerUser(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/AuthenticateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                Log.e("response", response);
                                if (jO.getString("ProfileFlag").contentEquals("1")) {
                                    editor.putString("edited", "1");
                                    editor.apply();
                                    receiveData();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    sendFCM(jO.getString("UserId"));
                                }else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    sendFCM(jO.getString("UserId"));
                                }
                            }else if(jO.getString("Status").contentEquals("404")){
                                Toast.makeText(LoginActivity.this, "Null Entries Sent", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "User doesn't exists!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email.replace(" ", "%20"));
                params.put("Password", password.replace(" ", "%20"));
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    private void receiveData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            SharedPreferences.Editor editor = sp.edit();
                            JSONObject jO = new JSONObject(response);
                            Log.e("response", jO.getString("Status"));
                            if (jO.getString("Status").contentEquals("200")){
                                editor.putString("user_name", jO.getString("Name"));
                                Log.e("user_name", jO.getString("Name"));
                                editor.apply();
                            }else{
                                //  if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(LoginActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.getStackTraceString(error);
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                params.put("UserId", sp.getString("uid", null));
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }


    void registerUserWithFb(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/CreateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE_login", response);
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                if (jO.getString("ProfileFlag").contentEquals("1"))
                                    editor.putString("edited", "1");
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                sendFCM(jO.getString("UserId"));
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(LoginActivity.this, "Email  already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "API ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email.replace(" ", "%20"));
                params.put("Password", password.replace(" ", "%20"));
                params.put("LoginFlag", loginFlag.replace(" ", "%20"));
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    public void functions(JSONObject obj) {
        if (obj.has("Status")){
            try {
                if (obj.getString("Status").contentEquals("200")){
                    Toast.makeText(LoginActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                    sendFCM(obj.getString("UserId"));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Error Uploading!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
Initialize the facebook sdk.
And then callback manager will handle the login responses.
*/
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
    }

     /*
  Register a callback function with LoginButton to respond to the login result.
 */

    protected void getLoginDetails(LoginButton login_button){

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                Log.e("TAG_FBSUCCESS", "");
                getUserInfo(login_result);

            }

            @Override
            public void onCancel() {
                Log.e("TAG_FB", "CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("TAG_FB", "sddud");
                Log.getStackTraceString(exception);
            }
        });
    }

    /*
To get the facebook user's own profile information via  creating a new request.
When the request is completed, a callback is called to handle the success condition.
*/
    protected void getUserInfo(LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            email =json_object.getString("email");
                            Log.e("emailID", email);
                            sendData(email, "ChatAroundWithPankaj", "1");
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }
                    }
                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields","id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    public void show(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return convertInputStreamToString(is, length);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    private void sendFCM(final String uid){
        if (sp.contains("token")) {
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                    getRequestQueue();
            Log.e("FCMToekn", "http://52.66.45.251:8080/GetFCMToken/UserId="+uid+"&Token="+ sp.getString("token", null));
            StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://52.66.45.251:8080/GetFCMToken",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                    Log.getStackTraceString(error);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("UserId", uid);
                    params.put("Token", sp.getString("token", null));
                    return params;
                }
            };
            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
        }
        else {
            Log.e("FIREBIRDTOKEN", FirebaseInstanceId.getInstance().getToken());
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                    getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/Post?UserId="+uid+"&Token="+FirebaseInstanceId.getInstance().getToken(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(LoginActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                    Log.getStackTraceString(error);
                }
            });
            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
        }
    }

}

/*
    /**
     * Shows the progress UI and hides the login form.

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    */