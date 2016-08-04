package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.ParticularPost;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.database.DBHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 23/7/16.
 */
public class Tweets_adapter extends BaseAdapter {
    private Context context;
    private JSONArray posts;
    int total = 0;
    DBHandler db;

    public Tweets_adapter(Context context, JSONArray posts){
        this.context = context;
        this.posts = posts;
        total = posts.length();
        db = new DBHandler(context);
    }
    @Override
    public int getCount() {
        return total;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_my_tweets, parent, false);
        RelativeLayout rl_tweet = (RelativeLayout)rowView.findViewById(R.id.rL_tweet);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user_post);
        TextView poster_name = (TextView) rowView.findViewById(R.id.poster_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        try {
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId=" + posts.getJSONObject(position).getString("PosterId")).resize(512, 512).into(user_img);
            poster_name.setText(posts.getJSONObject(position).getString("PosterName"));
            message.setText(posts.getJSONObject(position).getString("Post"));
            timestamp.setText(posts.getJSONObject(position).getString("TimeStamp").substring(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rl_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticularPost.class);
                try {
                    intent.putExtra("post_id", posts.getJSONObject(position).getString("PostId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public void refresh(JSONArray posts){
        this.posts = posts;
        total = posts.length();
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
