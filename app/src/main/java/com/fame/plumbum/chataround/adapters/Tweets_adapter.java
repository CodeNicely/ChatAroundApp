package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.ParticularPost;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.database.PostTable;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pankaj on 23/7/16.
 */
public class Tweets_adapter extends BaseAdapter {
    private Context context;
    private List<PostTable> posts;
    int total = 0;
    DBHandler db;

    public Tweets_adapter(Context context, List<PostTable> posts){
        this.context = context;
        this.posts = posts;
        total = posts.size();
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
        ImageView user_img = (ImageView) rowView.findViewById(R.id.image_user_post);
        TextView poster_name = (TextView) rowView.findViewById(R.id.poster_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        Picasso.with(context).load(""+db.getUser(posts.get(position).getPoster_id()).getImage()).into(user_img);
        poster_name.setText(posts.get(position).getPoster_name());
        message.setText(posts.get(position).getMessage());
        timestamp.setText(posts.get(position).getTimestamp());
        rl_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticularPost.class);
                intent.putExtra("post_id", posts.get(position).getPost_id());
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public void refresh(List<PostTable> posts){
        this.posts = posts;
        total = posts.size();
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
