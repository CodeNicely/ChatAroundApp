package com.fame.plumbum.chataround.shouts;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.Urls;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pankaj on 22/7/16.
 */
public class ShoutsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int MAX_RETRIES = 5;
    private static final String TAG = "WORLD";
    View rootView = null;

    ShoutsAdapter adapter;
    ListView listView;


    public int count = 0;
    private SharedPreferences sharedPreferences;
    private SharedPrefs sharedPrefs;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;

    @BindView(R.id.shout)
    Button shout;

    public SwipeRefreshLayout swipeRefreshLayout;
    public double lat, lng;
    private JSONArray[] currentListOfPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_world, container, false);
        context = getContext();

        ButterKnife.bind(this, rootView);
        sharedPrefs=new SharedPrefs(context);
        currentListOfPost = new JSONArray[]{new JSONArray()};

        listView = (ListView) rootView.findViewById(R.id.world_tweets_list);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);


        Button load_more = new Button(context);


        load_more.setBackgroundResource(R.drawable.border_button);
        load_more.setAllCaps(true);
        load_more.setText("Load more");
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                getAllPosts(count);
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        listView.addFooterView(load_more);

      /*  swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getAllPosts(count);
                                    }
                                }*/
//        );

        shout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionShout();

            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        return rootView;
    }


/*
    public void getAllPosts(List<JSONObject> response, int count) {
        try {

            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
//            JSONObject jO = new JSONObject(response);
//            currentListOfPost[0] = jO.getJSONArray("Posts");

            JSONArray mine = new JSONArray();
//            currentListOfPost[0] = response;

            for (int i = 0; i < response.size(); i++)
                mine.put(response.get(i));
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                if (count == 0) {
                    midText.setVisibility(View.GONE);
                    adapter = new ShoutsAdapter(context, mine, lat, lng);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < mine.length(); i++)
                        adapter.posts.put(mine.getJSONObject(i));
                    adapter.total = adapter.total + mine.length();
                    int index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();

                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(index, top);
                }
            } else {
                listView.setVisibility(View.GONE);
                midText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ignored) {
        }
    }
*/


    public void getAllPosts(String response, int count) {
        try {
            JSONObject jO = new JSONObject(response);

            currentListOfPost[0] = jO.getJSONArray("Posts");
            JSONArray mine = new JSONArray();

            for (int i = 0; i < currentListOfPost[0].length(); i++)
                mine.put(currentListOfPost[0].getJSONObject(i));
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                if (count == 0) {
                    midText.setVisibility(View.GONE);
                    adapter = new ShoutsAdapter(context, mine, lat, lng);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < mine.length(); i++)
                        adapter.posts.put(mine.getJSONObject(i));
                    adapter.total = adapter.total + mine.length();
                    int index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();

                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(index, top);
                }
            } else {
                listView.setVisibility(View.GONE);
                midText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void onRefresh() {

        getAllPosts(count);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {


            //If everything went fine lets get latitude and longitude
            lat = location.getLatitude();
            lng = location.getLongitude();


            getAllPosts(count);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        getAllPosts(count);

    }

    public void getAllPosts(int counter) {
        RequestQueue queue = MySingleton.getInstance(context).
                getRequestQueue();
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, Urls.BASE_URL +
                "ShowPost?UserId=" + sharedPreferences.getString("uid", null) +
                "&Counter=" + counter +
                "&Latitude=" + lat +
                "&Longitude=" + lng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null && response.length() > 0) {
                                JSONObject jo = new JSONObject(response);

                                /*if (profile != null && profile.swipeRefreshLayout != null) {
                                    profile.swipeRefreshLayout.setRefreshing(false);
                                }*/
                                if (jo.getJSONArray("Posts").length() > 0) {

                                    getAllPosts(response, count);

                                    /*if (profile != null && profile.swipeRefreshLayout != null) {
                                        profile.getAllPosts(response, count);
                                    }*/
                                } else {
                                    Toast.makeText(context, "No more posts found!", Toast.LENGTH_SHORT).show();

                                    if (count > 0) count -= 1;
                                }
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        } catch (JSONException ignored) {

                            swipeRefreshLayout.setRefreshing(false);


 /*                           if (profile != null && profile.swipeRefreshLayout != null) {
                                profile.swipeRefreshLayout.setRefreshing(false);
                            }*/
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);

            /*    if (profile != null && profile.swipeRefreshLayout != null) {
                    profile.swipeRefreshLayout.setRefreshing(false);
                }*/
                Toast.makeText(context, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void actionShout() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_post);
        final EditText content = (EditText) dialog.findViewById(R.id.post_content);
        final TextView mTextView = (TextView) dialog.findViewById(R.id.num_chars);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mTextView.setText(String.valueOf(140 - s.length()) + "/140");
            }

            public void afterTextChanged(Editable s) {
            }
        };
        content.addTextChangedListener(mTextEditorWatcher);


        Button create_post = (Button) dialog.findViewById(R.id.post_button);
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, Urls.BASE_URL + "Post?UserId=" + profile.uid + "&UserName=" + profile.name.replace(" ", "%20") + "&Post=Hello&Latitude=" + lat + "&Longitude=" + lng, Toast.LENGTH_SHORT).show();
                Log.d(TAG, Urls.BASE_URL + "Post?UserId=" + sharedPreferences.getString("uid", null) + "&UserName=" + sharedPreferences.getString("user_name", "") + "&Post=Hello&Latitude=" + lat + "&Longitude=" + lng);
                String content_txt = content.getText().toString();
                if (lat != 0 && lng != 0) {
                    RequestQueue queue = MySingleton.getInstance(context).getRequestQueue();
                    content_txt = content_txt.replace("\n", "%0A");
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Post?UserId=" + sharedPreferences.getString("uid", null) + "&UserName=" + sharedPreferences.getString("user_name", "").replace(" ", "%20") + "&Post=" + content_txt.replace(" ", "%20") + "&Latitude=" + lat + "&Longitude=" + lng,
                            new Response.Listener<String>() {
                                public static final String TAG = "MainActivity";

                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "Response " + response);

                                    Answers.getInstance().logCustom(new CustomEvent("Adding Shout Successful")
                                            .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                                            .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                            .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    );
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Answers.getInstance().logCustom(new CustomEvent("Adding Shout Failed")
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                            );
                            Toast.makeText(context, "Error sending data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                } else {
                    Toast.makeText(context, "Location Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}