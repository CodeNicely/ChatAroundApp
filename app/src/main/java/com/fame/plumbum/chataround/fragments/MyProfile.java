package com.fame.plumbum.chataround.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;
import com.fame.plumbum.chataround.chat.MainWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 4/8/16.
 */
public class MyProfile extends Fragment {
    View rootView = null;
    public String uid, user_name;
    Tweets_adapter adapter;
    CircleImageView user_image;
    ListView listView;
    TextView name, phone;
    ImageView edit;

    double lat, lng;
    JSONArray[] completeListOfPosts;
    JSONArray[] currentListOfPost;

    SharedPreferences sharedPreferences;
    MainWindow activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_profile, container, false);
        listView = (ListView) rootView.findViewById(R.id.my_tweets_list);
        user_image = (CircleImageView) rootView.findViewById(R.id.image_user);
        name = (TextView) rootView.findViewById(R.id.name_user);
        phone = (TextView) rootView.findViewById(R.id.phone_user);
        edit = (ImageView) rootView.findViewById(R.id.edit_button);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;

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
