package com.fame.plumbum.chataround;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fame.plumbum.chataround.adapters.Comments_adapter;

import org.json.JSONArray;

/**
 * Created by pankaj on 23/7/16.
 */
public class ParticularPost extends AppCompatActivity implements View.OnClickListener {
    EditText add_comment;
    TextView post_title;
    ImageButton like, chat_button;
    String post_id = "", my_id = "", poster_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_post);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:

                break;
            case R.id.chat_button:
                Intent intent = new Intent(ParticularPost.this, ParticularChat.class);
                intent.putExtra("post_id", post_id);
                startActivity(intent);
                break;
            case R.id.like_button:

                break;
        }
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
        ListView comments_list = (ListView) findViewById(R.id.list_comments_post);
        add_comment = (EditText) findViewById(R.id.comment_add);
        post_title = (TextView) findViewById(R.id.post_title);
        Button sendComment = (Button) findViewById(R.id.add_button);
        like = (ImageButton) findViewById(R.id.like_button);
        chat_button = (ImageButton) findViewById(R.id.chat_button);
        Comments_adapter adapter = new Comments_adapter(this, new JSONArray());
        comments_list.setAdapter(adapter);
        sendComment.setOnClickListener(this);
        like.setOnClickListener(this);
        chat_button.setOnClickListener(this);
    }
}
