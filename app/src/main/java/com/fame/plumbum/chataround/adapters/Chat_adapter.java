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
import com.fame.plumbum.chataround.database.ChatTable;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 24/7/16.
 */
public class Chat_adapter extends BaseAdapter {
    private Context context;
    public List<ChatTable> chats;
    int total = 0;

    public Chat_adapter(Context context, List<ChatTable> chats){
        this.context = context;
        this.chats= chats;
        total = chats.size();
    }
    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setChats(ChatTable chat ) {
        chats.add(chat);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_chat, parent, false);
        RelativeLayout rl_caht = (RelativeLayout)rowView.findViewById(R.id.rL_chat);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user);
        TextView sender_name = (TextView) rowView.findViewById(R.id.sender_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        RelativeLayout rl_chat_sent = (RelativeLayout)rowView.findViewById(R.id.rL_chat_sent);
        ImageView user_img_sent = (ImageView) rowView.findViewById(R.id.image_user_sent);
        TextView sender_name_sent = (TextView) rowView.findViewById(R.id.sender_name_sent);
        TextView message_sent = (TextView) rowView.findViewById(R.id.message_sent);
        TextView timestamp_sent = (TextView) rowView.findViewById(R.id.timestamp_sent);
        if (chats.get(position).getStatus()==2) {
            rl_chat_sent.setVisibility(View.GONE);
            rl_caht.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(chats.get(position).getStatus()==1?chats.get(position).getId()).into(user_img);
            sender_name.setText(chats.get(position).getRemote_name());
            message.setText(chats.get(position).getMessage());
            timestamp.setText(chats.get(position).getTimestamp());
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId="+chats.get(position).getRemote_name()).error(R.drawable.user).into(user_img);
        }else{
            rl_chat_sent.setVisibility(View.VISIBLE);
            rl_caht.setVisibility(View.GONE);
//            Picasso.with(context).load(chats.get(position).getStatus()==1?chats.get(position).getId()).into(user_img_sent);
            sender_name_sent.setText(chats.get(position).getRemote_name());
            message_sent.setText(chats.get(position).getMessage());
            timestamp_sent.setText(chats.get(position).getTimestamp());
            Picasso.with(context).load("http://52.66.45.251:8080/ImageReturn?UserId="+chats.get(position).getRemote_name()).error(R.drawable.user).into(user_img_sent);
        }

        return rowView;
    }
}
