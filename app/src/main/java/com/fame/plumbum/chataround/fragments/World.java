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
import android.widget.ListView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;
import com.fame.plumbum.chataround.chat.MainWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pankaj on 22/7/16.
 */
public class World extends Fragment {
    View rootView = null;
    String uid, user_name;
    Tweets_adapter adapter;
    ListView listView;
    double lat, lng;
    JSONArray[] currentListOfPost;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = sharedPreferences.getString("uid", null);
        user_name = sharedPreferences.getString("user_name", null);
        currentListOfPost = new JSONArray[]{new JSONArray()};
        activity.needSomethingWorld = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_world, container, false);
        listView = (ListView) rootView.findViewById(R.id.world_tweets_list);
        return rootView;
    }

    public void getAllPosts(String response) {
        try {
            JSONObject jO = new JSONObject(response);
            JSONArray mine = new JSONArray();
            currentListOfPost[0] = jO.getJSONArray("Posts");
            for (int i = 0; i < currentListOfPost[0].length(); i++) {
                if (!currentListOfPost[0].getJSONObject(i).getString("PosterId").contentEquals(uid)) {
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