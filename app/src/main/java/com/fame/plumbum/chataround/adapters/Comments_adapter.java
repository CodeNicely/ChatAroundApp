package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.UserProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 24/7/16.
 */
public class Comments_adapter extends BaseAdapter {
    private Context context;
    public JSONArray comments;
    public int total = 0;

    public Comments_adapter(Context context, JSONArray comments){
        this.context = context;
        this.comments = comments;
        total = comments.length();
    }
    @Override
    public int getCount() {
        return total;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_post_comments, parent, false);
        RelativeLayout rl_comment = (RelativeLayout)rowView.findViewById(R.id.rL_comment);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user);
        TextView commentor_name = (TextView) rowView.findViewById(R.id.commentor_name);
        TextView comment = (TextView) rowView.findViewById(R.id.comment);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        try {
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId="+comments.getJSONObject(position).getString("CommentorId")).into(user_img);
            commentor_name.setText(comments.getJSONObject(position).getString("CommentorName"));
            comment.setText(comments.getJSONObject(position).getString("Comment"));
            timestamp.setText(comments.getJSONObject(position).getString("TimeStamp").substring(4));
        } catch (JSONException e) {
            Log.getStackTraceString(e);
        }
        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                try {
                    intent.putExtra("uid", comments.getJSONObject(position).getString("CommentorId"));
                } catch (JSONException e) {
                    Log.getStackTraceString(e);
                }
                context.startActivity(intent);
            }
        });
        return rowView;
    }
}
