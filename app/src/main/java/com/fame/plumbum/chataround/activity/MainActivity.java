package com.fame.plumbum.chataround.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.fame.plumbum.chataround.emergency.view.EmergencyFragment;
import com.fame.plumbum.chataround.helper.Constants;
import com.fame.plumbum.chataround.profile.ProfileFragment;
import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;
import com.fame.plumbum.chataround.referral.presenter.ReferralPresenter;
import com.fame.plumbum.chataround.referral.presenter.ReferralPresenterImpl;
import com.fame.plumbum.chataround.referral.provider.RetrofitReferralProvider;
import com.fame.plumbum.chataround.referral.view.ReferralView;
import com.fame.plumbum.chataround.shouts.view.ShoutsFragment;
import com.fame.plumbum.chataround.gallery.view.GalleryFragment;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import it.sephiroth.android.library.tooltip.Tooltip;

import static com.fame.plumbum.chataround.helper.MyApplication.getContext;

/**
 * Created by pankaj on 4/8/16.
 */
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ReferralView {
    private static final int MOCK_LOCATION_OFF_REQUEST = 201;

    private static final int FRAGMENT_TYPE_PROFILE = 0;
    private static final int FRAGMENT_TYPE_SHOUTS = 1;
    private static final int FRAGMENT_TYPE_TOILET = 2;
    private static final int FRAGMENT_TYPE_GALLERY = 3;
    private static final int FRAGMENT_TYPE_POLLUTION = 4;
    private static final int FRAGMENT_TYPE_NEWS = 5;
    private static final int FRAGMENT_TYPE_EMERGENCY=6;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private SharedPrefs sharedPrefs;
    public double lat, lng;
    BroadcastReceiver receiver;
    SharedPreferences sp;
    String token;
    private boolean gps_enabled;
    private boolean network_enabled;
    static final int LOCATION_SETTINGS_REQUEST = 1;

    private ViewPagerAdapter adapter;

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ReferralPresenter referralPresenter;

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


//        toolbar = getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitle(R.string.app_name);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
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
                IntentFilter filter = new IntentFilter("Hello ShoutsFragmentOld");
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().contentEquals("Hello ShoutsFragmentOld")) {
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
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);

            setupViewPager(viewPager);
            viewPager.setCurrentItem(adapter.getCount() - 1);
            adapter.setPrimaryItem(null, adapter.getCount() - 1, null);



          /*  if (viewPager.getAdapter().getCount() == 6) {

                if (tabLayout.getTabAt(0) != null) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.profile_512);
                }

                if (tabLayout.getTabAt(1) != null) {
                    tabLayout.getTabAt(1).setIcon(R.drawable.world);
                }
                if (tabLayout.getTabAt(2) != null) {

                    tabLayout.getTabAt(2).setIcon(R.drawable.restroom1);
                }
                if (tabLayout.getTabAt(3) != null) {

                    tabLayout.getTabAt(3).setIcon(R.drawable.gallery);
                }
                if (tabLayout.getTabAt(4) != null) {

                    tabLayout.getTabAt(4).setIcon(R.drawable.pollution1);
                }
                if (tabLayout.getTabAt(5) != null) {

                    tabLayout.getTabAt(5).setIcon(R.drawable.newspaper);
                }
            }
            viewPager.setOffscreenPageLimit(6);
*/


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
        ProfileFragment profile = new ProfileFragment();
        ShoutsFragment shoutsFragment = new ShoutsFragment();
        PollutionFragment pollutionFragment = new PollutionFragment();
        GalleryFragment galleryFragment = new GalleryFragment();
        NewsListFragment newsListFragment = new NewsListFragment();
        RestroomFragment restroomFragment = new RestroomFragment();
        EmergencyFragment emergencyFragment= new EmergencyFragment();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(profile, "My Profile", FRAGMENT_TYPE_PROFILE);

        if (sharedPrefs.isShouts()) {
            adapter.addFragment(shoutsFragment, "ShoutsFragmentOld", FRAGMENT_TYPE_SHOUTS);
        }

        if (sharedPrefs.isToilet()) {
            adapter.addFragment(restroomFragment, "RestRoomFragment", FRAGMENT_TYPE_TOILET);

        }
        if (sharedPrefs.isGallery()) {
            adapter.addFragment(galleryFragment, "GalleryFragment", FRAGMENT_TYPE_GALLERY);

        }
        if (sharedPrefs.isPullution()) {
            adapter.addFragment(pollutionFragment, "PollutionFragment", FRAGMENT_TYPE_POLLUTION);
        }
        if (sharedPrefs.isNews()) {
            adapter.addFragment(newsListFragment, "NewsListFragment", FRAGMENT_TYPE_NEWS);
        }
        if(sharedPrefs.isEmergency()){
            adapter.addFragment(emergencyFragment, "EmergencyFragment", FRAGMENT_TYPE_EMERGENCY);
        }


        upViewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < adapter.getCount(); i++) {

            switch (adapter.fragmentTypeList.get(i)) {

                case FRAGMENT_TYPE_PROFILE:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.profile_512);
                    }
                    break;
                case FRAGMENT_TYPE_SHOUTS:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.world);
                    }
                    break;
                case FRAGMENT_TYPE_TOILET:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.restroom1);
                    }
                    break;
                case FRAGMENT_TYPE_GALLERY:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.gallery);
                    }
                    break;
                case FRAGMENT_TYPE_POLLUTION:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.pollution1);
                    }
                    break;
                case FRAGMENT_TYPE_NEWS:
                    if (tabLayout.getTabAt(i) != null) {
                        tabLayout.getTabAt(i).setIcon(R.drawable.newspaper);
                    }
                    break;


            }
        }

        viewPager.setOffscreenPageLimit(adapter.getCount());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                switch (adapter.fragmentTypeList.get(position)) {
                    case FRAGMENT_TYPE_PROFILE:
                        toolbar.setTitle("Profile");
                       /* Answers.getInstance().logCustom(new CustomEvent("User Swiped to Profile")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())
                        );
*/
                        break;
                    case FRAGMENT_TYPE_SHOUTS:
                        toolbar.setTitle("Shouts");
  /*                      Answers.getInstance().logCustom(new CustomEvent("User Swiped to Shouts")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                        );
*/
                        break;
                    case FRAGMENT_TYPE_TOILET:
                        toolbar.setTitle("Restrooms");
  /*                      Answers.getInstance().logCustom(new CustomEvent("User Swiped to Restroom")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                        );
*/
                        break;

                    case FRAGMENT_TYPE_GALLERY:
                        toolbar.setTitle("Gallery");
  /*                      Answers.getInstance().logCustom(new CustomEvent("User Swiped to Gallery")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                        );
*/
                        break;

                    case FRAGMENT_TYPE_POLLUTION:
                        toolbar.setTitle("Pollution Meter");
  /*                      Answers.getInstance().logCustom(new CustomEvent("User Swiped to Pollution")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                        );
*/
                        break;
                    case FRAGMENT_TYPE_NEWS:
                        toolbar.setTitle("News");
  /*                      Answers.getInstance().logCustom(new CustomEvent("User Swiped to News")
                                .putCustomAttribute(Keys.KEY_LATITUDE, lat)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, lng)
                                .putCustomAttribute(Keys.USER_EMAIL, sharedPrefs.getEmail())

                        );
*/
                        break;
                    default:
                        toolbar.setTitle("1 Mile App");

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


    }

    public void reloadViewPager() {

        setupViewPager(viewPager);
        viewPager.setCurrentItem(0);
        adapter.setPrimaryItem(null, 0, null);
//        adapter.notifyDataSetChanged();

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

/*
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
*/

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (isMockLocation(location)) {
                    return;
                }
            }
            //If everything went fine lets get latitude and longitude
            lat = location.getLatitude();
            lng = location.getLongitude();

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

//                city = addresses.get(0).getLocality();
//                state = addresses.get(0).getAdminArea();
                if (addresses.size() > 0) {
                    String country = addresses.get(0).getCountryName();
                    if (country.contentEquals(Constants.KEY_COUNTRY_INDIA)) {


                        referralPresenter = new ReferralPresenterImpl(this, new RetrofitReferralProvider());

                        referralPresenter.requestDeviceIdVerify(sharedPrefs.getUserId(), Settings.Secure.getString(getContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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


    }

    @Override
    public void showDialogLoader(boolean show) {

    }

    @Override
    public void onDeviceDataReceived(VerifyDeviceData verifyDeviceData) {

        if (verifyDeviceData.isNew_device()) {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_referral);
            final EditText referal_code = (EditText) dialog.findViewById(R.id.referal);
            Button proceed = (Button) dialog.findViewById(R.id.proceed);
            Button skip = (Button) dialog.findViewById(R.id.skip);

            referal_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 10) {
                        hideKeyboard();
                    }
                }
            });

            dialog.setTitle("Referal Code");
            dialog.setCancelable(false);
            dialog.show();

            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String code = referal_code.getText().toString();
                    if (code.equals("") || code.equals(null)) {
                        referal_code.setError("Invalid mobile number");
                        referal_code.requestFocus();
                    } else if (code.length() < 10 || code.length() > 10) {
                        referal_code.setError("Invalid mobile number");
                        referal_code.requestFocus();
                    } else {

                        sharedPrefs.setUserMobile(code);
                        referralPresenter.requestReferal(sharedPrefs.getUserId(), Settings.Secure.getString(getContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID), code);
                        dialog.dismiss();
                    }

                }

            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {

            Log.d(TAG, "Old device");
        }

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> fragmentTypeList = new ArrayList<>();

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

        public void addFragment(Fragment fragment, String title, int fragmentType) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            fragmentTypeList.add(fragmentType);
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {

            super.setPrimaryItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
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
                                String newsDescription, String newsAuthor, String newsTimestamp, String newsUrl) {
        Intent newsDetailsActivityIntent = new Intent(this, NewsDetailsActivity.class);


        Bundle bundle = new Bundle();
        bundle.putString(Keys.NEWS_TITLE, newsTitle);
        bundle.putString(Keys.NEWS_IMAGE, image);
        bundle.putString(Keys.NEWS_SOURCE, newsSource);
        bundle.putString(Keys.NEWS_DESCRIPTION, newsDescription);
        bundle.putString(Keys.NEWS_AUTHOR, newsAuthor);
        bundle.putString(Keys.NEWS_TIMESTAMP, newsTimestamp);
        bundle.putString(Keys.NEWS_URL, newsUrl);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            // user is back from location settings - check if location services are now enabled

            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if (requestCode == MOCK_LOCATION_OFF_REQUEST) {

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isMockLocation(Location location) {
        if (location.isFromMockProvider()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            alertDialog.setTitle(R.string.mock_location_title)
                    .setMessage(R.string.mock_location_message)
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                ComponentName componentName = intent.resolveActivity(getPackageManager());
                                if (componentName == null) {
                                    Toast.makeText(getApplicationContext(), "No Activity to handle Intent action", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivityForResult(intent, MOCK_LOCATION_OFF_REQUEST);
                                }
                            } catch (Exception e) {

                                Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();

            return true;
        } else {
            return false;
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /*  @Override
      public boolean onPrepareOptionsMenu(final Menu menu) {
          getMenuInflater().inflate(R.menu.menu, menu);

          return super.onCreateOptionsMenu(menu);
      }
  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_help);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                String message = null;
                // write your code here
                if (viewPager.getCurrentItem() == FRAGMENT_TYPE_SHOUTS) {
                    message = "Bulletin Board for your 1-mile radius. Post messages for everyone in your area to see. " +
                            "See other people’s messages. Chat with them publicly through comments or privately through chat. ";
                } else if (viewPager.getCurrentItem() == FRAGMENT_TYPE_TOILET) {
                    message = "Public toilets(with photos and location) in your 1-mile radius sorted " +
                            "by nearest. Photos represent the toilets’ outside appearance and relative cleanliness. ";
                } else if (viewPager.getCurrentItem() == FRAGMENT_TYPE_GALLERY) {
                    message = "Images unique to your area clicked in your 1-mile radius (landmarks, offices, religious institutions etc) ";
                } else if (viewPager.getCurrentItem() == FRAGMENT_TYPE_POLLUTION) {
                    message = "Pollution statistics from your nearest air pollution sensor. Subject to " +
                            "availability of an digitally open air pollution sensor within 50km. ";

                } else if (viewPager.getCurrentItem() == FRAGMENT_TYPE_NEWS) {
                    message = "Every news article on the Internet related to your city or state in " +
                            "case there aren’t enough news articles relating to your city (in case " +
                            "of tier-2 and tier-3 cities).";
                }

                if (message != null) {
                    /*final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_help);
                    Button okay = (Button) dialog.findViewById(R.id.okay);
                    TextView messageTextView = (TextView) dialog.findViewById(R.id.description);


                    messageTextView.setText(message);

                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();*/



/*                    new SimpleTooltip.Builder(this)
                            .anchorView(toolbar.getChildAt(0))
                            .text(message)
                            .gravity(Gravity.BOTTOM)
                            .animated(true)
                            .transparentOverlay(false)
                            .build()
                            .show();*/

                    final Tooltip.Builder tooltip = new Tooltip.Builder(101)
                            .anchor(toolbar.getChildAt(0), Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, true), 10000)
                            .activateDelay(000)
                            .showDelay(000)
                            .text(message)
                            .maxWidth(ViewPager.LayoutParams.MATCH_PARENT)
                            .withArrow(true)
                            .withOverlay(true)
                            .withCallback(new Tooltip.Callback() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onTooltipClose(Tooltip.TooltipView tooltipView, boolean b, boolean b1) {
//                                            viewPager.setAlpha(Float.parseFloat("1"));
                                    viewPager.setAlpha(1);

                                }

                                @Override
                                public void onTooltipFailed(Tooltip.TooltipView tooltipView) {

                                }

                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onTooltipShown(Tooltip.TooltipView tooltipView) {
                                    viewPager.setAlpha(Float.parseFloat("0.5"));

                                }

                                @Override
                                public void onTooltipHidden(Tooltip.TooltipView tooltipView) {

                                }
                            })
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .build();


                    Tooltip.make(this, tooltip).show();
                }

                return true;
            default:
                return false;
        }
    }


}