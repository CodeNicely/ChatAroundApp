package com.fame.plumbum.chataround.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pankaj on 22/7/16.
 */
public class World extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView = null;
    String uid, user_name;
    Tweets_adapter adapter;
    ListView listView;
    JSONArray[] currentListOfPost;
    MainActivity activity;

    @BindView(R.id.shout)
    Button shout;

    public SwipeRefreshLayout swipeRefreshLayout;
    public double lat, lng;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = sharedPreferences.getString("uid", null);
        user_name = sharedPreferences.getString("user_name", null);
        currentListOfPost = new JSONArray[]{new JSONArray()};
        activity.needSomethingWorld = true;
        Button load_more = new Button(getContext());
        load_more.setBackgroundResource(R.drawable.border_button);
        load_more.setAllCaps(true);
        load_more.setText("Load more");
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.count = activity.count + 1;
                activity.needSomethingWorld = true;
                activity.needSomethingTweet = true;
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


        listView.addFooterView(load_more);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_world, container, false);

        ButterKnife.bind(this, rootView);

        listView = (ListView) rootView.findViewById(R.id.world_tweets_list);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        activity.needSomethingWorld = true;
                                    }
                                }
        );

        shout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof MainActivity) {
                    ((MainActivity) getContext()).actionShout();
                }
            }
        });

        return rootView;
    }


    public void getAllPosts(String response, int count) {
        try {
            JSONObject jO = new JSONObject(response);
            JSONArray mine = new JSONArray();
            currentListOfPost[0] = jO.getJSONArray("Posts");
            for (int i = 0; i < currentListOfPost[0].length(); i++)
                mine.put(currentListOfPost[0].getJSONObject(i));
            TextView midText = (TextView) rootView.findViewById(R.id.midText);
            if (mine.length() > 0) {
                if (count == 0) {
                    midText.setVisibility(View.GONE);
                    adapter = new Tweets_adapter(getContext(), mine, lat, lng);
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
        activity.needSomethingWorld = true;
    }
}