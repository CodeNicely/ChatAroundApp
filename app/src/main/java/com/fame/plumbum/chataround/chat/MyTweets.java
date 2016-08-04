package com.fame.plumbum.chataround.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pankaj on 22/7/16.
 */
public class MyTweets extends Fragment {

    View rootView = null;
    String uid, user_name;
    Tweets_adapter adapter;
    ListView listView;

    double lat, lng;
    JSONArray[] completeListOfPosts;
    JSONArray[] currentListOfPost;

    SharedPreferences sharedPreferences;
    MainWindow activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainWindow) getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = sharedPreferences.getString("uid", null);
        user_name = sharedPreferences.getString("user_name", null);
        completeListOfPosts = new JSONArray[]{new JSONArray()};
        currentListOfPost = new JSONArray[]{new JSONArray()};
        activity.needSomethingTweet = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_my_tweets, container, false);
        listView = (ListView) rootView.findViewById(R.id.my_tweets_list);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG_RESUME", "RESUMED");
        if (sharedPreferences != null) {
            user_name = sharedPreferences.getString("user_name", null);
        }
        activity.needSomethingTweet = true;
    }

    public void getAllPosts(String response) {
        try {
            JSONObject jO = new JSONObject(response);
            JSONArray mine = new JSONArray();
            currentListOfPost[0] = jO.getJSONArray("Posts");
            for (int i = 0; i < currentListOfPost[0].length(); i++) {
                if (currentListOfPost[0].getJSONObject(i).getString("PosterId").contentEquals(uid)) {
                    mine.put(currentListOfPost[0].getJSONObject(i));
                }
            }
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                midText.setVisibility(View.GONE);
                adapter = new Tweets_adapter(getContext(), mine);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
                midText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            Log.getStackTraceString(e);
        }
    }
}

//    private void getLoc(){
//        LocationRequest locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        PendingResult<Status> result = LocationServices.FusedLocationApi
//                .requestLocationUpdates(
//                        apiClient,   // your connected GoogleApiClient
//                        locationRequest,   // a request to receive a new location
//                        MyTweets.this); // the listener which will receive updated locations
//
//// Callback is asynchronous. Use await() on a background thread or listen for
//// the ResultCallback
//        result.setResultCallback(new ResultCallback<Status>() {
//            void onResult(Status status) {
//                if (status.isSuccess()) {
//                    // Successfully registered
//                } else if (status.hasResolution()) {
//                    // Google provides a way to fix the issue
//                    status.startResolutionForResult(
//                            activity,     // your current activity used to receive the result
//                            RESULT_CODE); // the result code you'll look for in your
//                    // onActivityResult method to retry registering
//                } else {
//                    // No recovery. Weep softly or inform the user.
//                    Log.e(TAG, "Registering failed: " + status.getStatusMessage());
//                }
//            }
//        });
//    }


/*
DBHandler db = new DBHandler(this);

// Inserting Shop/Rows
Log.d("Insert: ", "Inserting ..");
db.addShop(new Shop("Dockers", " 475 Brannan St #330, San Francisco, CA 94107, United States"));
db.addShop(new Shop("Dunkin Donuts", "White Plains, NY 10601"));
db.addShop(new Shop("Pizza Porlar", "North West Avenue, Boston , USA"));
db.addShop(new Shop("Town Bakers", "Beverly Hills, CA 90210, USA"));

// Reading all shops
Log.d("Reading: ", "Reading all shops..");
List<Shop> shops = db.getAllShops();

for (Shop shop : shops) {
String log = "Id: " + shop.getId() + " ,Name: " + shop.getName() + " ,Address: " + shop.getAddress();
// Writing shops to log
Log.d("Shop: : ", log);
}
}
}



protected void createLocationRequest() {
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(10000);
    mLocationRequest.setFastestInterval(5000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
}

 */


