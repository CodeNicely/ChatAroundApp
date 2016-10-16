package com.fame.plumbum.chataround.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsLogger;
import com.fame.plumbum.chataround.LocationService;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.models.ImageSendData;
import com.fame.plumbum.chataround.queries.ServerAPI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText pass_edit, email_edit;
    String password, email="", loginFlag = "0", photoUrl = "";
    SharedPreferences sp;
    RelativeLayout rl_progress;
    File file;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        facebookSDKInitialize();
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_SHORT).show();
            finish();
        }else
            callNec();
    }

    private void callNec() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            insertDummyContactWrapper();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        LoginActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            insertDummyContactWrapper();
                            break;
                    }
                }
            });             }
    }

    private void init(){
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }else {
            Intent intent_service = new Intent(LoginActivity.this, LocationService.class);
            startService(intent_service);
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (sp.contains("otp_verified")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (sp.contains("edited")) {
                Intent intent = new Intent(this, OTP.class);
                startActivity(intent);
                finish();
            } else if (sp.contains("uid")) {
                Intent intent = new Intent(this, GetProfileDetails.class);
                startActivity(intent);
                finish();
            }
        }
        rl_progress = (RelativeLayout)findViewById(R.id.progress_imageLoading);
        pass_edit = (EditText) findViewById(R.id.pass_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        Button button = (Button) findViewById(R.id.sign_up);
        Button login = (Button) findViewById(R.id.login);
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
                        rl_progress.setVisibility(View.VISIBLE);
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
//        fb_signin = (LoginButton) findViewById(R.id.fb_signin);
//        fb_signin.setLoginBehavior(LoginBehavior.WEB_ONLY);
//        fb_signin.setReadPermissions(Arrays.asList("public_profile", "email"));
//        fb_signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLoginDetails(fb_signin);
//            }
//        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 999);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void sendData(String email, String password, String s) {
        this.email = email;
        this.password = password;
        loginFlag = s;
        registerUserWithFb();
    }

    private void getDetails() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void receiveData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            SharedPreferences.Editor editor = sp.edit();
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                String names = jO.getString("Name").replace("%20", " ");
                                editor.putString("user_name", toProperCase(names));
                                editor.putString("user_phone", jO.getString("Mobile"));
                                editor.putString("otp_verified", "1");
                                editor.apply();
                                getDetails();
                            }
                        } catch (JSONException ignored) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError ignored) {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                params.put("UserId", sp.getString("uid", ""));
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    void registerUser(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/AuthenticateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            rl_progress.setVisibility(View.GONE);
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                if (jO.getString("ProfileFlag").contentEquals("1")) {
                                    editor.putString("edited", "1");
                                    editor.apply();
                                    receiveData();
                                }
                                rl_progress.setVisibility(View.GONE);
                            }else if(jO.getString("Status").contentEquals("404")){
                                rl_progress.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Null Entries Sent", Toast.LENGTH_SHORT).show();
                            }else{
                                rl_progress.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "User doesn't exists!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            rl_progress.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rl_progress.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email.replace(" ", "%20"));
                params.put("Password", password.replace(" ", "%20"));
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }


    void registerUserWithFb(){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/CreateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            rl_progress.setVisibility(View.GONE);
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                editor.apply();
                                if (jO.getString("ProfileFlag").contentEquals("1")){
                                    editor.putString("edited", "1");
                                    editor.apply();
                                    receiveData();
                                    rl_progress.setVisibility(View.GONE);
                                }else {
                                    Intent intent = new Intent(LoginActivity.this, GetProfileDetails.class);
                                    startActivity(intent);
                                    finish();
                                    rl_progress.setVisibility(View.GONE);
                                }
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(LoginActivity.this, "Email  already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "API ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ignored) {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rl_progress.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email);
                params.put("Password", password);
                params.put("LoginFlag", loginFlag);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        switch (requestCode) {
            case 1000:
                switch (responseCode) {
                    case Activity.RESULT_OK:
                        insertDummyContactWrapper();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
            case 999:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
                handleSignInResult(result);
                break;
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            rl_progress.setVisibility(View.VISIBLE);
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                email = acct.getEmail();
                if (email != null) {
                    if (email.length() == 0)
                        Toast.makeText(LoginActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                    else {
                        if (acct.getPhotoUrl()!=null) {
                            photoUrl = acct.getPhotoUrl().toString();
                            Target target = new Target(){
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    savebitmap(bitmap);
                                    sendDataWithPhoto(email, "ChatAroundWithPankaj", "1");
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Toast.makeText(LoginActivity.this, "Error retrieving Profile picture", Toast.LENGTH_SHORT).show();
                                    sendData(email, "ChatAroundWithPankaj", "1");
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            };
                            img = new ImageView(this);
                            img.setTag(target);
                            Picasso.with(LoginActivity.this).load(Uri.parse(photoUrl)).resize(256, 256).into(target);

                        }else
                            sendData(email, "ChatAroundWithPankaj", "1");
                    }
                }
            } else
                Toast.makeText(LoginActivity.this, "Unable to get details!", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(LoginActivity.this, "Unable to get details!", Toast.LENGTH_SHORT).show();
    }

    private void sendDataWithPhoto(final String email, final String password, final String loginFlag) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/CreateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("uid", jO.getString("UserId"));
                                editor.apply();
                                sendImage_one();
                                if (jO.getString("ProfileFlag").contentEquals("1")){
                                    editor.putString("edited", "1");
                                    editor.apply();
                                    receiveData();
                                    rl_progress.setVisibility(View.GONE);
                                }else {
                                    Intent intent = new Intent(LoginActivity.this, GetProfileDetails.class);
                                    startActivity(intent);
                                    finish();
                                    rl_progress.setVisibility(View.GONE);
                                }
                            }else if(jO.getString("Status").contentEquals("400")){
                                rl_progress.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Email  already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                rl_progress.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "API ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            rl_progress.setVisibility(View.GONE);
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
                params.put("Email", email);
                params.put("Password", password);
                params.put("LoginFlag", loginFlag);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);

    }


    //3. How To Upload image using retrofit.
    // This is pretty straight forward. So not commenting.
    void sendImage_one() {
        final SharedPreferences.Editor editor = sp.edit();
        ServerAPI api = ServerAPI.retrofit.create(ServerAPI.class);

        RequestBody to_server = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),
                to_server);

        api.upload(sp.getString("uid", ""), body).enqueue(new Callback<ImageSendData>() {
            @Override
            public void onResponse(Call<ImageSendData> call, retrofit2.Response<ImageSendData> response) {
                ImageSendData formSever = response.body();
                if (formSever.getStatus()==200){
                    Toast.makeText(LoginActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    editor.putString("image", "1");
                    editor.apply();
                }else{
                    Toast.makeText(LoginActivity.this, "Error uploading image!", Toast.LENGTH_SHORT).show();
                }
                rl_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ImageSendData> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error uploading image!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savebitmap(Bitmap bitmap) {
        FileOutputStream fOut;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pickImageResult.jpg");
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);

            fOut.flush();
            fOut.close();
        } catch (IOException ignored) {
        }

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
        mGoogleApiClient.disconnect();
    }

    private String toProperCase(String name) {
        if (name!=null && name.length()>0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            for (int i = 0; ; ) {
                i = name.indexOf(" ", i + 1);
                if (i < 0)
                    break;
                else {
                    if (i < name.length()-2)
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i + 2);
                    else if (i == name.length()-2) {
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                        break;
                    }
                }
            }
        }
        return name;
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("FINE LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        init();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    init();
                } else {
                    Toast.makeText(LoginActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}