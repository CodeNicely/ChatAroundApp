package com.fame.plumbum.chataround.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.fame.plumbum.chataround.activity.ParticularChat;
import com.fame.plumbum.chataround.activity.ParticularPost;
import com.fame.plumbum.chataround.activity.SelfChatList;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.helper.Urls;
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
 * Created by pankaj on 23/7/16.
 */
public class Tweets_adapter extends BaseAdapter {
    private Context context;
    public JSONArray posts;
    public int total = 0;
    DBHandler db;
    Typeface typeFace, typeface_light;
    double lat, lng;

    public Tweets_adapter(Context context, JSONArray posts, double lat, double lng) {
        this.context = context;
        this.posts = posts;
        total = posts.length();
        db = new DBHandler(context);
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_thin.ttf");
        typeface_light = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_light.ttf");
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int getCount() {
        return total;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_my_tweets, parent, false);
        LinearLayout chat = (LinearLayout) rowView.findViewById(R.id.chat);
//        final LinearLayout reply = (LinearLayout)rowView.findViewById(R.id.reply);
        CircleImageView chat_dot = (CircleImageView) rowView.findViewById(R.id.chat_dot);
        final LinearLayout report = (LinearLayout) rowView.findViewById(R.id.report);
        final TextView num_post = (TextView) rowView.findViewById(R.id.num_post);
        final ImageView report_image = (ImageView) rowView.findViewById(R.id.report_image);
        RelativeLayout rl_tweet = (RelativeLayout) rowView.findViewById(R.id.rL_tweet);
        CircleImageView user_img = (CircleImageView) rowView.findViewById(R.id.image_user_post);
        TextView poster_name = (TextView) rowView.findViewById(R.id.poster_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        message.setTypeface(typeFace);
        poster_name.setTypeface(typeFace);
        final CircleImageView report_dot = (CircleImageView) rowView.findViewById(R.id.report_dot);
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_proifle);
                Button okay = (Button) dialog.findViewById(R.id.okay);
                CircleImageView image_user = (CircleImageView) dialog.findViewById(R.id.image_user);
                TextView name = (TextView) dialog.findViewById(R.id.name);
                TextView phone = (TextView) dialog.findViewById(R.id.phone);
                LinearLayout ll_phone = (LinearLayout) dialog.findViewById(R.id.ll_phone);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                try {
                    if (!sp.getString("uid", "").contentEquals(posts.getJSONObject(position).getString("PosterId"))) {
                        ll_phone.setVisibility(View.INVISIBLE);
                    }
                    getImage(posts.getJSONObject(position).getString("PosterId"), true, image_user);
                    receiveData(posts.getJSONObject(position).getString("PosterId"), name, phone);

                } catch (JSONException e) {
                    e.printStackTrace();
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

        try {
            if (db.getPeronalChats(posts.getJSONObject(position).getString("PostId")).size() > 0)
                chat_dot.setVisibility(View.VISIBLE);
            else
                chat_dot.setVisibility(View.GONE);
            int reports = posts.getJSONObject(position).getJSONArray("Like").length();
            int comments = posts.getJSONObject(position).getJSONArray("Comments").length();
            if (reports == 0 && comments == 0) {
                num_post.setText("No entries found.");
            } else if (reports == 0) {
                num_post.setText(comments + " comments");
            } else if (comments == 0) {
                num_post.setText(reports + " reports");
            } else {
                num_post.setText(comments + " comments and " + reports + " reports");
            }
            if (!posts.getJSONObject(position).getString("NoOfLikes").contentEquals("0")) {
                report_dot.setVisibility(View.VISIBLE);
            } else {
                report_dot.setVisibility(View.GONE);
            }
        } catch (JSONException ignored) {
        }
        num_post.setVisibility(View.VISIBLE);
        try {
            if (posts.getJSONObject(position).getString("LikeFlag").contentEquals("0"))
                report_image.setBackgroundResource(R.drawable.thumbs_down_accent);
            else
                report_image.setBackgroundResource(R.drawable.thumbs_down_red);
            getImage(posts.getJSONObject(position).getString("PosterId"), false, user_img);
            poster_name.setText(toProperCase(posts.getJSONObject(position).getString("PosterName").replace("%20", " ")));
            message.setText(posts.getJSONObject(position).getString("Post").replace("%20", " "));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
            Date date = sdf.parse(posts.getJSONObject(position).getString("TimeStamp"));
            long millis = date.getTime();
            long current_millis = (new Date()).getTime();
            int minutes = (int) ((current_millis - millis) / 60000);
            int hours = (int) (((current_millis - millis) / 60000) / 60);
            int days = (int) (((current_millis - millis) / 3600000) / 24);
            if (minutes < 60) {
                timestamp.setText(minutes + " min ago");
            } else if (hours < 24) {
                timestamp.setText(hours + " hrs ago");
            } else {
                timestamp.setText(days + " days ago");
            }
            if (!posts.getJSONObject(position).getString("NoOfLikes").contentEquals("0")) {
                report_dot.setVisibility(View.VISIBLE);
            } else {
                report_dot.setVisibility(View.GONE);
            }
        } catch (JSONException | ParseException ignored) {
        }
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (posts.getJSONObject(position).getString("LikeFlag").contentEquals("0")) {
                        posts.getJSONObject(position).put("LikeFlag", "1");
                        report_image.setBackgroundResource(R.drawable.thumbs_down_red);
                    } else {
                        posts.getJSONObject(position).put("LikeFlag", "0");
                        report_image.setBackgroundResource(R.drawable.thumbs_down_accent);
                    }

                    RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).
                            getRequestQueue();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Like?UserId=" + sp.getString("uid", "") + "&PostId=" + posts.getJSONObject(position).getString("PostId") + "&UserName=" + sp.getString("user_name", "").replace(" ", "%20") + "&Latitude=" + lat + "&Longitude=" + lng,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error sending data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String poster_id = null;
                try {
                    poster_id = posts.getJSONObject(position).getString("PosterId");
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    if (!poster_id.contentEquals(sp.getString("uid", ""))) {
                        final Intent intent = new Intent(context, ParticularChat.class);
                        intent.putExtra("post_id", posts.getJSONObject(position).getString("PostId"));
                        intent.putExtra("uid_r", poster_id);
                        intent.putExtra("poster_name", posts.getJSONObject(position).getString("PosterId").replace("%20", " "));
                        intent.putExtra("remote_name", posts.getJSONObject(position).getString("PosterId").replace("%20", " "));
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, SelfChatList.class);
                        intent.putExtra("post_id", posts.getJSONObject(position).getString("PostId"));
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        rl_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticularPost.class);
                try {
                    intent.putExtra("post_id", posts.getJSONObject(position).getString("PostId"));
                    context.startActivity(intent);
                } catch (JSONException ignored) {
                }
            }
        });
        return rowView;
    }

    private String getImage(String uid, final boolean big, final CircleImageView user_img) {
        final String[] image_name = new String[1];
        MySingleton.getInstance(context.getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "ImageName?UserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Tweets Adapter ",json.toString());
                            if (json.getInt("Status") == 200) {
                                image_name[0] = json.getString("ImageName");
                                if (big)
                                {
                                    picassoBig(image_name[0], user_img);
                                }
                                else
                                {
                                    picassoSmall(image_name[0], user_img);
                                }
                            }else if(json.getInt("Status")==400){

                                Toast.makeText(context, "Status 400 for ImageName Api", Toast.LENGTH_SHORT).show();

                            }
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

    private void picassoSmall(String s, CircleImageView user_img) {
        Picasso.with(context).load(Urls.BASE_URL + "ImageReturn?ImageName=" + s).resize(256, 256).error(R.drawable.user).into(user_img);
    }

    private void picassoBig(String s, CircleImageView user_img) {
        Picasso.with(context).load(Urls.BASE_URL + "ImageReturn?ImageName=" + s).resize(512, 512).error(R.drawable.user_big).into(user_img);
    }

    private String toProperCase(String name) {
        if (name != null && name.length() > 0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            for (int i = 0; ; ) {
                i = name.indexOf(" ", i + 1);
                if (i < 0)
                    break;
                else {
                    if (i < name.length() - 2)
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i + 2);
                    else if (i == name.length() - 2) {
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                        break;
                    }
                }
            }
        }
        return name;
    }

    public void refresh(JSONArray posts) {
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

    private void receiveData(final String uid, final TextView name, final TextView phone) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Urls.BASE_URL + "GetProfile",
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
                        } catch (JSONException e) {
                            e.printStackTrace();
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