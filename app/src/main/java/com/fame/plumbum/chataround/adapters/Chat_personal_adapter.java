package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.ParticularChat;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.database.ChatTable;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 29/7/16.
 */
public class Chat_personal_adapter extends BaseAdapter {
    private Context context;
    public List<ChatTable> chatTableList;
    public int total = 0;

    public Chat_personal_adapter(Context context, List<ChatTable> chatTableList){
        this.context = context;
        this.chatTableList = chatTableList;
        total = chatTableList.size();
    }
    @Override
    public int getCount() {
        return chatTableList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_post_comments, parent, false);
        RelativeLayout rl_comment = (RelativeLayout)rowView.findViewById(R.id.rL_comment);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user);
        TextView commentor_name = (TextView) rowView.findViewById(R.id.commentor_name);
        TextView comment = (TextView) rowView.findViewById(R.id.comment);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        if (chatTableList.get(position).getStatus()==1) {
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId=" + sp.getString("uid", null)).error(R.drawable.user).into(user_img);
            commentor_name.setText(chatTableList.get(position).getPoster_name());
        }else{
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId=" + chatTableList.get(position).getRemote_id()).error(R.drawable.user).into(user_img);
            commentor_name.setText(chatTableList.get(position).getRemote_name());
        }
        comment.setText(chatTableList.get(position).getMessage());
        timestamp.setText(chatTableList.get(position).getTimestamp());
        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticularChat.class);
                intent.putExtra("uid_r", chatTableList.get(position).getRemote_id());
                intent.putExtra("post_id", chatTableList.get(position).getPost_id());
                intent.putExtra("poster_name", chatTableList.get(position).getPoster_name());
                intent.putExtra("remote_name", chatTableList.get(position).getRemote_name());
                context.startActivity(intent);
            }
        });
        return rowView;
    }
}
