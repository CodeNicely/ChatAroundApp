package com.fame.plumbum.chataround.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.adapters.Chat_personal_adapter;
import com.fame.plumbum.chataround.database.DBHandler;

/**
 * Created by pankaj on 29/7/16.
 */
public class SelfChatList extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_peronal_chat);
        String post_id = getIntent().getExtras().getString("post_id");
        ListView list_personal = (ListView) findViewById(R.id.list_personal);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(this);
        Chat_personal_adapter adapter = new Chat_personal_adapter(this, db.getPeronalChats(post_id));
        db.close();
        list_personal.setAdapter(adapter);
    }
}
