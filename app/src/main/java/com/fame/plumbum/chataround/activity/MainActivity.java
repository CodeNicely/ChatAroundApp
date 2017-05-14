package com.fame.plumbum.chataround.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.fragments.MyProfile;
import com.fame.plumbum.chataround.fragments.World;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.image_viewer.ImageViewerActivity;
import com.fame.plumbum.chataround.news.view.NewsDetailsActivity;
import com.fame.plumbum.chataround.news.view.NewsListFragment;
import com.fame.plumbum.chataround.pollution.view.PollutionFragment;
import com.fame.plumbum.chataround.restroom.view.RestroomFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by pankaj on 4/8/16.
 */
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int MAX_RETRIES = 5;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private SharedPrefs sharedPrefs;
    private static final String TAG = "MainActivity";
    public double lat, lng;
    public boolean needSomethingTweet = false, needSomethingWorld = false;
    MyProfile profile;
    World world;
    BroadcastReceiver receiver;
    SharedPreferences sp;
    public int count = 0;
    String token;
    private boolean gps_enabled;
    private boolean network_enabled;
    static final int LOCATION_SETTINGS_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = new SharedPrefs(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();


        final ActionBar toolbar = getSupportActionBar();

        toolbar.setTitle(R.string.app_name);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            dialog.setCancelable(false);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);


                    startActivityForResult(myIntent, LOCATION_SETTINGS_REQUEST);
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            dialog.show();
        } else {

            /*if (receiver == null) {
                IntentFilter filter = new IntentFilter("Hello World");
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().contentEquals("Hello World")) {
                            lat = intent.getDoubleExtra("lat", 0.0);
                            lng = intent.getDoubleExtra("lng", 0.0);
                            world.lat = lat;
                            world.lng = lng;

                            profile.lat = lat;
                            profile.lng = lng;

                            if (needSomethingTweet || needSomethingWorld) {
                                needSomethingWorld = false;
                                needSomethingTweet = false;
                                getAllPosts(count);
                            }
                        }
                    }
                };
                registerReceiver(receiver, filter);
            }*/
            Fabric.with(this, new Crashlytics());
            logUser();

            Answers.getInstance().logCustom(new CustomEvent("User Opened App"));


            sp = PreferenceManager.getDefaultSharedPreferences(this);
            initFCM();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            if( viewPager.getAdapter().getCount()==5) {

                if(tabLayout.getTabAt(0)!=null){
                    tabLayout.getTabAt(0).setIcon(R.drawable.profile_512);
                }

                if(tabLayout.getTabAt(1)!=null) {
                    tabLayout.getTabAt(1).setIcon(R.drawable.world);
                }
                if(tabLayout.getTabAt(2)!=null) {

                    tabLayout.getTabAt(2).setIcon(R.drawable.restroom1);
                }
                if(tabLayout.getTabAt(3)!=null) {

                    tabLayout.getTabAt(3).setIcon(R.drawable.pollution1);
                }
                if(tabLayout.getTabAt(4)!=null) {

                    tabLayout.getTabAt(4).setIcon(R.drawable.newspaper);
                }
            }
            viewPager.setOffscreenPageLimit(5);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                    switch (position) {
                        case 0:
                            toolbar.setTitle("Profile");
                            Answers.getInstance().logCustom(new CustomEvent("User Swiped to Profile")
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                            );

                            break;
                        case 1:
                            toolbar.setTitle("Shouts");
                            Answers.getInstance().logCustom(new CustomEvent("User Swiped to Shouts")
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                            );

                            break;
                        case 2:
                            toolbar.setTitle("Restrooms");
                            Answers.getInstance().logCustom(new CustomEvent("User Swiped to Restroom")
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                            );

                            break;
                        case 3:
                            toolbar.setTitle("Pollution Meter");
                            Answers.getInstance().logCustom(new CustomEvent("User Swiped to Pollution")
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                            );

                            break;
                        case 4:
                            toolbar.setTitle("News");
                            Answers.getInstance().logCustom(new CustomEvent("User Swiped to News")
                                    .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                    .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                            );

                            break;
                        default:
                            toolbar.setTitle("Profile");

                            break;
                    }


                }

                @Override
                public void onPageSelected(int position) {


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            viewPager.setCurrentItem(4);
        }


    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(sharedPrefs.getUserId());
        Crashlytics.setUserEmail(sharedPrefs.getEmail());
        Crashlytics.setUserName(sharedPrefs.getUsername());
    }

    private void initFCM() {
        if (!sp.contains("token")) {
            SharedPreferences.Editor editor = sp.edit();
            if (FirebaseInstanceId.getInstance() != null) {
                token = FirebaseInstanceId.getInstance().getToken();
                if (token != null) {
                    editor.putString("token", token);
                    editor.apply();
                    sendFCM(sp.getString("uid", ""));
                }
            }
        } else {
            sendFCM(sp.getString("uid", ""));
        }
    }

    public void setupViewPager(ViewPager upViewPager) {
        profile = new MyProfile();
        world = new World();
        RestroomFragment restroomFragment = new RestroomFragment();
        PollutionFragment pollutionFragment = new PollutionFragment();
        NewsListFragment newsListFragment = new NewsListFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(profile, "My Profile");
        adapter.addFragment(world, "World");
        adapter.addFragment(new RestroomFragment().newInstance("", ""), "RestRoomFragment");
        adapter.addFragment(pollutionFragment, "PollutionFragment");
        adapter.addFragment(newsListFragment, "NewsListFragment");

        adapter.setPrimaryItem(null, 4, null);

        upViewPager.setAdapter(adapter);
    }

/*
    public void getAllPosts(int counter) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(RetrofitCache.provideCache())
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShoutsRequestApi shoutsRequestApi = retrofit.create(ShoutsRequestApi.class);
        Call<ShoutData1> call = shoutsRequestApi.requestShouts(profile.uid, counter, lat, lng);

        call.enqueue(new Callback<ShoutData1>() {
            @Override
            public void onResponse(Call<ShoutData1> call, retrofit2.Response<ShoutData1> response) {

                if (world != null && world.swipeRefreshLayout != null) {
                    world.swipeRefreshLayout.setRefreshing(false);
                }
                if (profile != null && profile.swipeRefreshLayout != null) {
                    profile.swipeRefreshLayout.setRefreshing(false);
                }

                if (response.body().getPosts().size() > 0) {
                    if (world != null && world.swipeRefreshLayout != null) {
                        world.getAllPosts(response.body().getPosts(), count);
                    }
                    if (profile != null && profile.swipeRefreshLayout != null) {
                        profile.getAllPosts(response.body().getPosts(), count);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No more posts found!", Toast.LENGTH_SHORT).show();
                    needSomethingTweet = false;
                    needSomethingWorld = false;
                    if (count > 0) count -= 1;
                }

            }

            @Override
            public void onFailure(Call<ShoutData1> call, Throwable t) {
                t.printStackTrace();

                if (world != null && world.swipeRefreshLayout != null) {
                    world.swipeRefreshLayout.setRefreshing(false);
                }
                if (profile != null && profile.swipeRefreshLayout != null) {
                    profile.swipeRefreshLayout.setRefreshing(false);
                }

                Toast.makeText(MainActivity.this, "Error receiving data!"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
*/

    public void getAllPosts(int counter) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, Urls.BASE_URL +
                "ShowPost?UserId=" + profile.uid +
                "&Counter=" + counter +
                "&Latitude=" + lat +
                "&Longitude=" + lng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null && response.length() > 0) {
                                JSONObject jo = new JSONObject(response);
                                if (world != null && world.swipeRefreshLayout != null) {
                                    world.swipeRefreshLayout.setRefreshing(false);
                                }
                                if (profile != null && profile.swipeRefreshLayout != null) {
                                    profile.swipeRefreshLayout.setRefreshing(false);
                                }
                                if (jo.getJSONArray("Posts").length() > 0) {
                                    if (world != null && world.swipeRefreshLayout != null) {
                                        world.getAllPosts(response, count);
                                    }
                                    if (profile != null && profile.swipeRefreshLayout != null) {
                                        profile.getAllPosts(response, count);
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "No more posts found!", Toast.LENGTH_SHORT).show();
                                    needSomethingTweet = false;
                                    needSomethingWorld = false;
                                    if (count > 0) count -= 1;
                                }
                            }
                        } catch (JSONException ignored) {
                            if (world != null && world.swipeRefreshLayout != null) {
                                world.swipeRefreshLayout.setRefreshing(false);
                            }
                            if (profile != null && profile.swipeRefreshLayout != null) {
                                profile.swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (world != null && world.swipeRefreshLayout != null) {
                    world.swipeRefreshLayout.setRefreshing(false);
                }
                if (profile != null && profile.swipeRefreshLayout != null) {
                    profile.swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(MainActivity.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            world.lat = lat;
            world.lng = lng;

            profile.lat = lat;
            profile.lng = lng;

            if (needSomethingTweet || needSomethingWorld) {
                needSomethingWorld = false;
                needSomethingTweet = false;
                getAllPosts(count);
            }

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

        if(world!=null) {
            world.lat = lat;
            world.lng = lng;
        }
        if(profile!=null) {
            profile.lat = lat;
            profile.lng = lng;
        }
        if (needSomethingTweet || needSomethingWorld) {
            needSomethingWorld = false;
            needSomethingTweet = false;
            getAllPosts(count);
        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /*@Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }
*/
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
  /*          if (mobject != object) {
                mObject=objet; //mObject is global variable in adapter
                //You can update your based on your logic like below
                View view == (LinearLayout) object;
                form4(view);
            }
  */
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }


    public void actionShout() {

        final Dialog dialog = new Dialog(MainActivity.this);
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

                Toast.makeText(MainActivity.this, Urls.BASE_URL + "Post?UserId=" + profile.uid + "&UserName=" + profile.name.replace(" ", "%20") + "&Post=Hello&Latitude=" + lat + "&Longitude=" + lng, Toast.LENGTH_SHORT).show();
                Log.d(TAG, Urls.BASE_URL + "Post?UserId=" + profile.uid + "&UserName=" + profile.name + "&Post=Hello&Latitude=" + lat + "&Longitude=" + lng);
                String content_txt = content.getText().toString();
                if (lat != 0 && lng != 0) {
                    RequestQueue queue = MySingleton.getInstance(MainActivity.this.getApplicationContext()).
                            getRequestQueue();
                    content_txt = content_txt.replace("\n", "%0A");
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Post?UserId=" + profile.uid + "&UserName=" + profile.name.replace(" ", "%20") + "&Post=" + content_txt.replace(" ", "%20") + "&Latitude=" + lat + "&Longitude=" + lng,
                            new Response.Listener<String>() {
                                public static final String TAG = "MainActivity";

                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "Response " + response);
                                    needSomethingTweet = true;
                                    Answers.getInstance().logCustom(new CustomEvent("Adding Shout Successful")
                                            .putCustomAttribute(Keys.USER_EMAIL,sharedPrefs.getEmail())
                                            .putCustomAttribute(Keys.KEY_LATITUDE,lat)
                                            .putCustomAttribute(Keys.KEY_LONGITUDE,lng)
                                    );
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Answers.getInstance().logCustom(new CustomEvent("Adding Shout Failed")
                                    .putCustomAttribute(Keys.USER_EMAIL,sharedPrefs.getEmail())
                                    .putCustomAttribute(Keys.KEY_LATITUDE,lat)
                                    .putCustomAttribute(Keys.KEY_LONGITUDE,lng)
                            );
                            Toast.makeText(MainActivity.this, "Error sending data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
                } else {
                    Toast.makeText(MainActivity.this, "Location Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null) {
            IntentFilter filter = new IntentFilter("Hello World");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().contentEquals("Hello World")) {
                        lat = intent.getDoubleExtra("lat", 0.0);
                        lng = intent.getDoubleExtra("lng", 0.0);
                        world.lat = lat;
                        world.lng = lng;
                        if (needSomethingTweet || needSomethingWorld) {
                            needSomethingWorld = false;
                            needSomethingTweet = false;
                            getAllPosts(count);
                        }
                    }
                }
            };
            registerReceiver(receiver, filter);
        }
    }

    private void sendFCM(final String uid) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.BASE_URL + "GetFCMToken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Notifications not working!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", uid);
                params.put("Token", sp.getString("token", ""));
                return params;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    public void openGoogleMaps(double latitude, double longitude) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
        startActivity(intent);
    }

    public void openImageViewer(ArrayList<String> urlList, int position) {

        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putStringArrayListExtra(Keys.KEY_IMAGE_LIST, urlList);
        intent.putExtra(Keys.KEY_POSITION_IMAGE, position);
        startActivity(intent);
    }

    public void openImageViewer(String url, int position) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(url);

        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putStringArrayListExtra(Keys.KEY_IMAGE_LIST, arrayList);
        intent.putExtra(Keys.KEY_POSITION_IMAGE, position);
        startActivity(intent);
    }

    public void openNewsDetails(String newsTitle, String image, String newsSource,
                                String newsDescription, String newsAuthor, String newsTimestamp) {
        Intent newsDetailsActivityIntent = new Intent(this, NewsDetailsActivity.class);


        Bundle bundle = new Bundle();
        bundle.putString(Keys.NEWS_TITLE, newsTitle);
        bundle.putString(Keys.NEWS_IMAGE, image);
        bundle.putString(Keys.NEWS_SOURCE, newsSource);
        bundle.putString(Keys.NEWS_DESCRIPTION, newsDescription);
        bundle.putString(Keys.NEWS_AUTHOR, newsAuthor);
        bundle.putString(Keys.NEWS_TIMESTAMP, newsTimestamp);

        newsDetailsActivityIntent.putExtras(bundle);

        startActivity(newsDetailsActivityIntent);

    }

/*    public void addFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //     getSupportActionBar().setTitle(title);
        }

    }*/

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            // user is back from location settings - check if location services are now enabled

            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
    }

}