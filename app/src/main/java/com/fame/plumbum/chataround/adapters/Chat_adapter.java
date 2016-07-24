package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by pankaj on 24/7/16.
 */
public class Chat_adapter extends BaseAdapter {
    private Context context;
    private JSONArray chats;
    int total = 0;

    public Chat_adapter(Context context, JSONArray chats){
        this.context = context;
        this.chats= chats;
        total = chats.length();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_post_comments, parent, false);
        RelativeLayout rl_caht = (RelativeLayout)rowView.findViewById(R.id.rL_chat);
        ImageView user_img = (ImageView) rowView.findViewById(R.id.image_user);
        TextView sender_name = (TextView) rowView.findViewById(R.id.sender_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        try {
            Picasso.with(context).load(""+chats.getJSONObject(position).getString("image")).into(user_img);
            sender_name.setText(chats.getJSONObject(position).getString("commentor_name"));
            message.setText(chats.getJSONObject(position).getString("comment"));
            timestamp.setText(chats.getJSONObject(position).getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        rl_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, );
//            }
//        });
        return rowView;
    }
}
