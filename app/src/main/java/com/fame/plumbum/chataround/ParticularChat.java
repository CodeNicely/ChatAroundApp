package com.fame.plumbum.chataround;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.adapters.Chat_adapter;
import com.fame.plumbum.chataround.database.ChatTable;
import com.fame.plumbum.chataround.database.DBHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pankaj on 24/7/16.
 */
public class ParticularChat extends AppCompatActivity{
    String post_id, uid_r, poster_name, remote_name;
    ListView chat_list;
    boolean gotNotif = false;
    Chat_adapter adapter;
    List<ChatTable> chatTables;
    String time_created;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particulat_chat);
        init();
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
        final Button sendChat = (Button) findViewById(R.id.add_button);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_chat_text = add_chat.getText().toString();
                if (add_chat.length()>0){
                    add_chat.setText("");
                    sendChat(add_chat_text.replace(" ", "%20"));
                }
            }
        });
        refresh();
    }

    private void refresh(){
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(post_id, uid_r);
        adapter = new Chat_adapter(this, chatTables);
        //adapter.chats.add(new ChatTable());
        db.close();
        chat_list.setAdapter(adapter);
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
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        time_created = sdf.format(new Date());
        Log.e("TAG_PARTICULAR_Chat", "http://52.66.45.251:8080/SendMessage?SenderId="+sp.getString("uid", null)+"&ReceiverId="+uid_r+"&SenderName="+sp.getString("user_name", null)+"&Message="+message+"&CreatedAt=" + time_created +"&epochTime=" + time_created +"&PostId="+post_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251:8080/SendMessage?SenderId="+sp.getString("uid", null)+"&ReceiverId="+uid_r+"&SenderName="+sp.getString("user_name", null).replace(" ", "%20")+"&Message="+message.replace(" ", "%20")+"&CreatedAt=" + sdf.format(new Date()).replace(" ", "%20") + "&epochTime=" + sdf.format(new Date()).replace(" ", "%20") +"&PostId="+post_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONMSE_CHAT", response);
                            JSONObject jO = new JSONObject(response);
                            getDetails(jO, sp.getString("uid", ""), message);
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularChat.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(ParticularChat.this).addToRequestQueue(stringRequest);
    }

    private void getDetails(JSONObject jO, String uid, String message) {
        DBHandler db = new DBHandler(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int status = 1;
        Log.e("TAG", "ADDING TO DATABASE");
        ChatTable chatTable = new ChatTable(status, post_id, uid_r, poster_name, remote_name, message.replace("%20", " "), time_created);
        db.addChat(chatTable);
        db.close();
        chatTables.add(chatTable);
        adapter.chats = chatTables;
        adapter.notifyDataSetChanged();
    }
}
