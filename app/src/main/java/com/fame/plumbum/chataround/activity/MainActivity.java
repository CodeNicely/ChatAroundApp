package com.fame.plumbum.chataround.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pankaj on 4/8/16.
 */
public class MainActivity extends AppCompatActivity{
    public double lat, lng;
    public boolean needSomethingTweet = false, needSomethingWorld = false;
    MyProfile profile;
    World world;
    BroadcastReceiver receiver;
    SharedPreferences sp;

    boolean canSendToken = false;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window);

        if (receiver==null) {
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
                            getAllPosts(0);
                        }
                    }
                }
            };
            registerReceiver(receiver, filter);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        initFCM();
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.user);
        tabLayout.getTabAt(1).setIcon(R.drawable.world);

        viewPager.setCurrentItem(1);
    }

    private void initFCM() {
        if (!sp.contains("token")){
            SharedPreferences.Editor editor = sp.edit();
            if (FirebaseInstanceId.getInstance()!=null){
                token = FirebaseInstanceId.getInstance().getToken();
                if (token != null) {
                    editor.putString("token", token);
                    editor.apply();
                    sendFCM(sp.getString("uid", ""));
                }
            }
        }else {
            sendFCM(sp.getString("uid", ""));
            Log.e("token:", sp.getString("token", ""));
        }
    }

    public void setupViewPager(ViewPager upViewPager) {
        profile = new MyProfile();
        world = new World();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(profile, "My Profile");
        adapter.addFragment(world, "World");
        upViewPager.setAdapter(adapter);
    }

    public void getAllPosts(int counter){
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/ShowPost?UserId=" + profile.uid + "&Counter=" + counter + "&Latitude=" + lat + "&Longitude=" + lng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (world!=null && world.swipeRefreshLayout!=null) {
                            world.swipeRefreshLayout.setRefreshing(false);
                            world.getAllPosts(response);
                        }
                        if (profile!=null && profile.swipeRefreshLayout!=null) {
                            profile.swipeRefreshLayout.setRefreshing(false);
                            profile.getAllPosts(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                world.swipeRefreshLayout.setRefreshing(false);
                profile.swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_inside, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_shout){
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
                    mTextView.setText(String.valueOf(140-s.length())+"/140");
                }

                public void afterTextChanged(Editable s) {
                }
            };
            content.addTextChangedListener(mTextEditorWatcher);


            Button create_post = (Button) dialog.findViewById(R.id.post_button);
            create_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content_txt = content.getText().toString();
                    if (lat != 0 && lng != 0) {
                        RequestQueue queue = MySingleton.getInstance(MainActivity.this.getApplicationContext()).
                                getRequestQueue();
                        content_txt = content_txt.replace("\n", "%0A");
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/Post?UserId=" + profile.uid + "&UserName=" + profile.name + "&Post=" + content_txt.replace(" ", "%20") + "&Latitude=" + lat + "&Longitude=" + lng,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        needSomethingTweet = true;
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver==null) {
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
                            getAllPosts(0);
                        }
                    }
                }
            };
            registerReceiver(receiver, filter);
        }
    }

    private void sendFCM(final String uid){
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                    getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://52.66.45.251/GetFCMToken",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Notifications not working!", Toast.LENGTH_SHORT).show();
                }
            }){
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
        if (receiver!=null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}