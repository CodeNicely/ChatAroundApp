package com.fame.plumbum.chataround.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 24/7/16.
 */
public class Comments_adapter extends BaseAdapter {
    private Context context;
    public JSONArray comments;
    public int total = 0;
    Typeface typeFace, typeface_light;

    public Comments_adapter(Context context, JSONArray comments){
        this.context = context;
        this.comments = comments;
        total = comments.length();
        typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_thin.ttf");
        typeface_light = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_light.ttf");
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
        final TextView comment = (TextView) rowView.findViewById(R.id.comment);
        comment.setTypeface(typeFace);
        commentor_name.setTypeface(typeface_light);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        try {
            getImage(comments.getJSONObject(position).getString("CommentorId"), false, user_img);
            commentor_name.setText(toProperCase(comments.getJSONObject(position).getString("CommentorName")));
            comment.setText(comments.getJSONObject(position).getString("Comment").replace("%20", " "));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
            Date date = sdf.parse(comments.getJSONObject(position).getString("TimeStamp"));
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
        } catch (JSONException | ParseException ignored) {
        }
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_proifle);
                Button okay = (Button) dialog.findViewById(R.id.okay);
                CircleImageView image_user = (CircleImageView)dialog.findViewById(R.id.image_user);
                TextView name = (TextView)dialog.findViewById(R.id.name);
                TextView phone = (TextView)dialog.findViewById(R.id.phone);
                LinearLayout ll_phone = (LinearLayout) dialog.findViewById(R.id.ll_phone);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                try {
                    if (!sp.getString("uid", "").contentEquals(comments.getJSONObject(position).getString("CommentorId"))){
                        ll_phone.setVisibility(View.INVISIBLE);
                    }
                    getImage(comments.getJSONObject(position).getString("CommentorId"), true, image_user);
                    receiveData(comments.getJSONObject(position).getString("CommentorId"), name, phone);
                } catch (JSONException ignored) {
                }
                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return rowView;
    }

    private String getImage(String uid, final boolean big, final CircleImageView user_img) {
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
                            if (big)
                                picassoBig(image_name[0], user_img);
                            else
                                picassoSmall(image_name[0], user_img);
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

    private void picassoBig(String s, CircleImageView user_img) {
        Picasso.with(context).load("http://52.66.45.251/ImageReturn?ImageName="+s).error(R.drawable.user_big).into(user_img);
    }

    private void picassoSmall(String s, CircleImageView user_img) {
        Picasso.with(context).load("http://52.66.45.251/ImageReturn?ImageName="+s).error(R.drawable.user).into(user_img);
    }

    private String toProperCase(String name) {
        if (name!=null && name.length()>0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            for (int i = 0; ; ) {
                i = name.indexOf(" ", i + 1);
                if (i < 0)
                    break;
                else {
                    if (i < name.length()-2)
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i + 2);
                    else if (i == name.length()-2) {
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                        break;
                    }
                }
            }
        }
        return name;
    }

    private void receiveData(final String uid, final TextView name, final TextView phone) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")) {
                                name.setText(jO.getString("Name").replace("%20", " "));
                                phone.setText(jO.getString("Mobile").replace("%20", " "));
                            } else if (jO.getString("Status").contentEquals("400")) {
                                Toast.makeText(context, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ignored) {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", uid);
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(myReq);
    }
}