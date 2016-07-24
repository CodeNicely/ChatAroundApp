package com.fame.plumbum.chataround.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;

import org.json.JSONArray;

/**
 * Created by pankaj on 22/7/16.
 */
public class World extends Fragment {
    View rootView;

    public World(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_my_tweets, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.my_tweets_list);
        Tweets_adapter adapter = new Tweets_adapter(getContext(), new JSONArray());
        listView.setAdapter(adapter);

        return rootView;
    }
}
