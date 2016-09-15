package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.ParticularChat;
import com.fame.plumbum.chataround.database.ChatTable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 29/7/16.
 */
public class Chat_personal_adapter extends BaseAdapter {
    private Context context;
    public List<ChatTable> chatTableList;
    public int total = 0;
    Typeface typeface_light;

    public Chat_personal_adapter(Context context, List<ChatTable> chatTableList){
        this.context = context;
        this.chatTableList = chatTableList;
        total = chatTableList.size();
        typeface_light = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_light.ttf");
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
        View rowView = inflater.inflate(R.layout.list_person, parent, false);
        RelativeLayout rl_comment = (RelativeLayout)rowView.findViewById(R.id.rL_comment);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user);
        TextView commentor_name = (TextView) rowView.findViewById(R.id.commentor_name);
        commentor_name.setTypeface(typeface_light);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        if (chatTableList.get(position).getStatus()==1) {
            getImage(sp.getString("uid", ""), true, user_img);
            commentor_name.setText(toProperCase(chatTableList.get(position).getPoster_name().replace("%20", " ")));
        }else{
            getImage(chatTableList.get(position).getRemote_id(), false, user_img);
            commentor_name.setText(toProperCase(chatTableList.get(position).getRemote_name().replace("%20", " ")));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(chatTableList.get(position).getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        long current_millis = (new Date()).getTime();
        int minutes = (int) ((current_millis - millis)/60000);
        int hours = (int) (((current_millis - millis)/60000)/60);
        int days = (int) (((current_millis - millis)/3600000)/24);
        if (minutes<60){
            timestamp.setText(minutes + " min ago");
        }else if (hours<24){
            timestamp.setText(hours + " hrs ago");
        }else{
            timestamp.setText(days + " days ago");
        }
        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticularChat.class);
                intent.putExtra("uid_r", chatTableList.get(position).getRemote_id());
                intent.putExtra("post_id", chatTableList.get(position).getPost_id());
                intent.putExtra("poster_name", toProperCase(chatTableList.get(position).getPoster_name().replace("%20", " ")));
                intent.putExtra("remote_name", toProperCase(chatTableList.get(position).getRemote_name().replace("%20", " ")));
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    private String getImage(String uid, final boolean local, final CircleImageView user_img) {
        final String[] image_name = new String[1];
        MySingleton.getInstance(context.getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.66.45.251/ImageName?UserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            image_name[0] = json.getString("ImageName");
                            if (local)
                                picassoLocal(image_name[0], user_img);
                            else
                                picassoGlobal(image_name[0], user_img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        return image_name[0];
    }

    private void picassoGlobal(String s, CircleImageView user_img) {
        Picasso.with(context).load("http://52.66.45.251/ImageReturn?ImageName=" + s).error(R.drawable.user).into(user_img);
    }

    private void picassoLocal(String s, CircleImageView user_img) {
        Picasso.with(context).load("http://52.66.45.251/ImageReturn?ImageName=" + s).error(R.drawable.user).into(user_img);
    }

    private String toProperCase(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        for (int i =0;;){
            i = name.indexOf(" ", i+1);
            if (i<0)
                break;
            else {
                if (i > name.length()-1)
                    name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                else
                    name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i+2);
            }
        }
        return name;
    }
}
