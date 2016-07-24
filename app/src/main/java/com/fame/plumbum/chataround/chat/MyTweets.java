package com.fame.plumbum.chataround.chat;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Tweets_adapter;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.database.PostTable;
import com.fame.plumbum.chataround.models.LocationChanges;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by pankaj on 22/7/16.
 */
public class MyTweets extends Fragment {

    View rootView = null;
    Button create_post;
    String uid, user_name;
    Tweets_adapter adapter;
    ListView listView;
    DBHandler db;

    public MyTweets(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = sharedPreferences.getString("uid", null);
        user_name = sharedPreferences.getString("user_name", null);
        db = new DBHandler(getContext());
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_create_post);
                final EditText content = (EditText) dialog.findViewById(R.id.post_content);
                Button create_post = (Button) dialog.findViewById(R.id.create_post);
                create_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content_txt = content.getText().toString();
                        LocationChanges locationChanges = new LocationChanges(getActivity());
                        final double[] loc = locationChanges.getLocation();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://52.66.45.251:8080/Post?UserId="+uid+"&UserName="+user_name+"&Post="+content_txt+"&Latitude="+loc[0]+"&Longitude="+loc[1],
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jO = new JSONObject(response);
                                            db.addPost(new PostTable(jO.getString("PostId"), uid, user_name, content_txt, 0, "", loc[0]+"", loc[1]+"", ""));
                                            adapter.refresh(db.getAllPosts());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Error sending data!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        LocationChanges locationChanges = new LocationChanges(getActivity());
        final double[] loc = locationChanges.getLocation();
        final JSONArray[] currentListOfPost = {new JSONArray()};
        final JSONArray[] completeListOfPosts = {new JSONArray()};
        do {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251:8080/ShowPost?UserId=" + uid + "&Counter=" + counter + "&Latitude=" + loc[0] + "&Longitude=" + loc[1],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                    for (int i = 0; i<currentListOfPost[0].length(); i++)
                                        completeListOfPosts[0].put(currentListOfPost[0].getJSONObject(i));
                                JSONObject jO = new JSONObject(response);
                                currentListOfPost[0] = jO.getJSONArray("Posts");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error sending data!", Toast.LENGTH_SHORT).show();
                }
            });
        }while (currentListOfPost[0].length()>0);
        
        adapter = new Tweets_adapter(getContext(), db.getAllPosts());
        listView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_my_tweets, container, false);
        listView = (ListView) rootView.findViewById(R.id.my_tweets_list);
        create_post = (Button) rootView.findViewById(R.id.create_post);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
}


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


 */