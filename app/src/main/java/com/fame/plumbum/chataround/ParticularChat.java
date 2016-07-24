package com.fame.plumbum.chataround;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fame.plumbum.chataround.adapters.Chat_adapter;

import org.json.JSONArray;

/**
 * Created by pankaj on 24/7/16.
 */
public class ParticularChat extends AppCompatActivity{


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
        ListView chat_list = (ListView) findViewById(R.id.list_chat);
        EditText add_chat = (EditText) findViewById(R.id.chat_add);
        Button sendChat = (Button) findViewById(R.id.add_button);
        Chat_adapter adapter = new Chat_adapter(this, new JSONArray());
        chat_list.setAdapter(adapter);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
