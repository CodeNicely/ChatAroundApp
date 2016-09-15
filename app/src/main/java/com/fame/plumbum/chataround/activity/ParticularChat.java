package com.fame.plumbum.chataround.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Chat_adapter;
import com.fame.plumbum.chataround.database.ChatTable;
import com.fame.plumbum.chataround.database.DBHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 24/7/16.
 */
public class ParticularChat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    String post_id, uid_r, poster_name, remote_name;
    ListView chat_list;
    Chat_adapter adapter;
    List<ChatTable> chatTables;
    String time_created;
    public SwipeRefreshLayout swipeRefreshLayout;
    MyReceiver receiver;

    @Override
    public void onRefresh() {
        refreshUpdate();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particulat_chat);
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter("MyReceiver"));
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("MyReceiver"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        chat_list = (ListView) findViewById(R.id.list_chat);
        final EditText add_chat = (EditText) findViewById(R.id.chat_add);
        uid_r = getIntent().getExtras().getString("uid_r");
        post_id = getIntent().getExtras().getString("post_id");
        poster_name = getIntent().getExtras().getString("poster_name");
        remote_name = getIntent().getExtras().getString("remote_name");
        final ImageButton sendChat = (ImageButton) findViewById(R.id.add_button);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        refreshUpdate();
                                    }
                                }
        );
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_chat_text = add_chat.getText().toString();
                if (add_chat_text.length()>0){
                    add_chat.setText("");
                    sendChat(add_chat_text.replace(" ", "%20"));
                }
            }
        });
        refresh();
    }

    private void refresh(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(post_id, uid_r);
        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
        getImage(sp.getString("uid", ""), true, user_img_self);
        getImage(uid_r, false, user_img_remote);
        adapter = new Chat_adapter(this, chatTables);
        //adapter.chats.add(new ChatTable());
        db.close();
        chat_list.setAdapter(adapter);
    }

    private String getImage(String uid, final boolean local, final CircleImageView user_img) {
        final String[] image_name = new String[1];
        MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/ImageName?UserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            image_name[0] = json.getString("ImageName");
                            if (local)
                                picassoLocal(image_name[0], user_img);
                            else
                                picassoGlobal(image_name[0], user_img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularChat.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ParticularChat.this).addToRequestQueue(stringRequest);
        return image_name[0];
    }

    private void picassoGlobal(String s, CircleImageView user_img) {
        Picasso.with(this).load("http://52.66.45.251/ImageReturn?ImageName="+s).resize(256,256).error(R.drawable.user).into(user_img);
    }

    private void picassoLocal(String s, CircleImageView user_img) {
        Picasso.with(this).load("http://52.66.45.251/ImageReturn?ImageName="+s).resize(256,256).error(R.drawable.user).into(user_img);
    }

    private void refreshUpdate(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(post_id, uid_r);
        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
        getImage(sp.getString("uid", ""), true, user_img_self);
        getImage(uid_r, false, user_img_remote);
        adapter = new Chat_adapter(this, chatTables);
        //adapter.chats.add(new ChatTable());
        db.close();
        chat_list.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chat_list.setAdapter(adapter);
                chat_list.setSelection(adapter.getCount() - 1);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void sendChat(final String message){
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        time_created = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/SendMessage?SenderId="+sp.getString("uid", null)+"&ReceiverId="+uid_r+"&SenderName="+sp.getString("user_name", null).replace(" ", "%20")+"&Message="+message.replace(" ", "%20")+"&CreatedAt=" + sdf.format(new Date()).replace(" ", "%20") + "&epochTime=" + sdf.format(new Date()).replace(" ", "%20") +"&PostId="+post_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            getDetails(message);
                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularChat.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ParticularChat.this).addToRequestQueue(stringRequest);
    }

    private void getDetails(String message) {
        DBHandler db = new DBHandler(this);
        int status = 1;
        ChatTable chatTable = new ChatTable(status, post_id, uid_r, poster_name.replace("%20", " "), remote_name.replace("%20", " "), message.replace("%20", " "), time_created.replace("%20", " "));
        db.addChat(chatTable);
        db.close();
        chatTables.add(chatTable);
        adapter.chats = chatTables;
        adapter.notifyDataSetChanged();
        chat_list.setSelection(adapter.getCount() - 1);
    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().getString("PostId").contentEquals(post_id)) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                DBHandler db = new DBHandler(ParticularChat.this);
                ChatTable chatTable = new ChatTable(2, intent.getExtras().getString("PostId"), intent.getExtras().getString("SenderId"), intent.getExtras().getString("PosterName"), intent.getExtras().getString("SenderName"), intent.getExtras().getString("Message"), intent.getExtras().getString("CreatedAt"));
                db.addChat(chatTable);
                db.close();
                chatTables.add(chatTable);
                adapter.chats = chatTables;
                adapter.notifyDataSetChanged();
                chat_list.setSelection(adapter.getCount() - 1);
            }
        }
    }
}