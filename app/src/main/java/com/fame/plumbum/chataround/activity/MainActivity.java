package com.fame.plumbum.chataround.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.fragments.MyProfile;
import com.fame.plumbum.chataround.fragments.World;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.image_viewer.ImageViewerActivity;
import com.fame.plumbum.chataround.news.view.NewsDetailsActivity;
import com.fame.plumbum.chataround.news.view.NewsListFragment;
import com.fame.plumbum.chataround.pollution.view.PollutionFragment;
import com.fame.plumbum.chataround.restroom.view.RestroomFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pankaj on 4/8/16.
 */
public class MainActivity extends AppCompatActivity {
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
            }
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            initFCM();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.profile_512);
            tabLayout.getTabAt(1).setIcon(R.drawable.world);
            tabLayout.getTabAt(2).setIcon(R.drawable.restroom1);
            tabLayout.getTabAt(3).setIcon(R.drawable.pollution1);
            tabLayout.getTabAt(4).setIcon(R.drawable.newspaper);

            viewPager.setOffscreenPageLimit(5);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                    switch (position) {
                        case 0:
                            toolbar.setTitle("Profile");
                            break;
                        case 1:
                            toolbar.setTitle("Shouts");
                            break;
                        case 2:
                            toolbar.setTitle("Restrooms");

                            break;
                        case 3:
                            toolbar.setTitle("Pollution Meter");

                            break;
                        case 4:
                            toolbar.setTitle("News");

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

        upViewPager.setAdapter(adapter);
    }

    public void getAllPosts(int counter) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "ShowPost?UserId=" + profile.uid + "&Counter=" + counter + "&Latitude=" + lat + "&Longitude=" + lng,
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
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

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
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