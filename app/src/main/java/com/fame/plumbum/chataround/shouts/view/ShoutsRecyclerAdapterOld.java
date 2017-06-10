/*
package com.fame.plumbum.chataround.shouts.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.ParticularChat;
import com.fame.plumbum.chataround.activity.ParticularPost;
import com.fame.plumbum.chataround.activity.SelfChatList;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.shouts.view.ShoutsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

*/
/**
 * Created by meghalagrawal on 10/06/17.
 *//*


public class ShoutsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MAX_RETRIES = 5;
    private Context context;
    public JSONArray posts = new JSONArray();
    private DBHandler db;
    private Typeface typeFace, typeface_light;
    private double lat = 0.0, lng = 0.0;
    private LayoutInflater layoutInflater;
    GlideImageLoader imageLoader;
    private ShoutsFragmentOld shoutsFragment;

    public ShoutsRecyclerAdapter(ShoutsFragmentOld shoutsFragment,Context context) {
        this.context = context;
        db = new DBHandler(context);
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_thin.ttf");
        typeface_light = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_light.ttf");
        layoutInflater = LayoutInflater.from(context);
        imageLoader=new GlideImageLoader(context);
        this.shoutsFragment=shoutsFragment;
    }

    public void setData(JSONArray posts) {
        this.posts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_my_tweets, parent, false);
        return new ShoutsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        try {
            final JSONObject post = posts.getJSONObject(position);


            final ShoutsViewHolder shoutsViewHolder = (ShoutsViewHolder) holder;

            shoutsViewHolder.message.setTypeface(typeFace);
            shoutsViewHolder.poster_name.setTypeface(typeFace);
            shoutsViewHolder.user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        shoutsFragment.showProfileDialog(posts.getJSONObject(position).getString("PosterName")
                                ,Urls.BASE_URL + "ImageReturn?ImageName=" +posts.getJSONObject(position).getString("PosterImage"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                if (db.getPeronalChats(post.getString("PostId")).size() > 0)
                    shoutsViewHolder.chat_dot.setVisibility(View.VISIBLE);
                else
                    shoutsViewHolder.chat_dot.setVisibility(View.GONE);
                int reports = post.getJSONArray("Like").length();
                int comments = post.getJSONArray("Comments").length();
                if (reports == 0 && comments == 0) {
                    shoutsViewHolder.num_post.setText("No entries found.");
                } else if (reports == 0) {
                    shoutsViewHolder.num_post.setText(comments + " comments");
                } else if (comments == 0) {
                    shoutsViewHolder.num_post.setText(reports + " reports");
                } else {
                    shoutsViewHolder.num_post.setText(comments + " comments and " + reports + " reports");
                }
                if (!post.getString("NoOfLikes").contentEquals("0")) {
                    shoutsViewHolder.report_dot.setVisibility(View.VISIBLE);
                } else {
                    shoutsViewHolder.report_dot.setVisibility(View.GONE);
                }
            } catch (JSONException ignored) {
            }
            shoutsViewHolder.num_post.setVisibility(View.VISIBLE);
            try {
                if (post.getString("LikeFlag").contentEquals("0"))
                    shoutsViewHolder.report_image.setBackgroundResource(R.drawable.thumbs_down_accent);
                else
                    shoutsViewHolder.report_image.setBackgroundResource(R.drawable.thumbs_down_red);
                imageLoader.loadImage(Urls.BASE_URL + "ImageReturn?ImageName=" +post.getString("PosterImage"),shoutsViewHolder.user_img,null);
                shoutsViewHolder.poster_name.setText(toProperCase(post.getString("PosterName").replace("%20", " ")));
                shoutsViewHolder.message.setText(post.getString("Post").replace("%20", " "));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = sdf.parse(post.getString("TimeStamp"));
                long millis = date.getTime();
                long current_millis = (new Date()).getTime();
                int minutes = (int) ((current_millis - millis) / 60000);
                int hours = (int) (((current_millis - millis) / 60000) / 60);
                int days = (int) (((current_millis - millis) / 3600000) / 24);
                if (minutes < 60) {
                    shoutsViewHolder.timestamp.setText(minutes + " min ago");
                } else if (hours < 24) {
                    shoutsViewHolder.timestamp.setText(hours + " hrs ago");
                } else {
                    shoutsViewHolder.timestamp.setText(days + " days ago");
                }
                if (!post.getString("NoOfLikes").contentEquals("0")) {
                    shoutsViewHolder.report_dot.setVisibility(View.VISIBLE);
                } else {
                    shoutsViewHolder.report_dot.setVisibility(View.GONE);
                }
            } catch (JSONException | ParseException ignored) {
            }
            shoutsViewHolder.report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (post.getString("LikeFlag").contentEquals("0")) {
                            post.put("LikeFlag", "1");
                            shoutsViewHolder.report_image.setBackgroundResource(R.drawable.thumbs_down_red);
                        } else {
                            post.put("LikeFlag", "0");
                            shoutsViewHolder.report_image.setBackgroundResource(R.drawable.thumbs_down_accent);
                        }

                        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "Like?UserId=" + sp.getString("uid", "") + "&PostId=" + post.getString("PostId") + "&UserName=" + sp.getString("user_name", "").replace(" ", "%20") + "&Latitude=" + lat + "&Longitude=" + lng,
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
            shoutsViewHolder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String poster_id = null;
                    try {
                        poster_id = post.getString("PosterId");
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        if (!poster_id.contentEquals(sp.getString("uid", ""))) {
                            final Intent intent = new Intent(context, ParticularChat.class);
                            intent.putExtra("post_id", post.getString("PostId"));
                            intent.putExtra("uid_r", poster_id);
                            intent.putExtra("poster_name", post.getString("PosterId").replace("%20", " "));
                            intent.putExtra("remote_name", post.getString("PosterId").replace("%20", " "));
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, SelfChatList.class);
                            intent.putExtra("post_id", post.getString("PostId"));
                            context.startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            shoutsViewHolder.rl_tweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ParticularPost.class);
                    try {
                        intent.putExtra("post_id", post.getString("PostId"));
                        context.startActivity(intent);
                    } catch (JSONException ignored) {
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }

 */
/*   private String getImage(String uid, final boolean big, final CircleImageView user_img) {
        final String[] image_name = new String[1];
        MySingleton.getInstance(context.getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + "ImageName?UserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Tweets Adapter ", json.toString());
                            if (json.getInt("Status") == 200) {
                                image_name[0] = json.getString("ImageName");
                                if (big) {
                                    picassoBig(image_name[0], user_img);
                                } else {
                                    picassoSmall(image_name[0], user_img);
                                }
                            } else if (json.getInt("Status") == 400) {

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
    }*//*


    */
/*   private void picassoSmall(String s, CircleImageView user_img) {
           Picasso.with(context).load(Urls.BASE_URL + "ImageReturn?ImageName=" + s).resize(256, 256).error(R.drawable.user).into(user_img);
       }

       private void picassoBig(String s, CircleImageView user_img) {
           Picasso.with(context).load(Urls.BASE_URL + "ImageReturn?ImageName=" + s).resize(512, 512).error(R.drawable.user_big).into(user_img);
       }
   *//*

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

    */
/*private void receiveData(final String uid, final TextView name, final TextView phone) {
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
        myReq.setRetryPolicy(new DefaultRetryPolicy(10000, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(myReq);
    }*//*


    public class ShoutsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat)
        LinearLayout chat;

        @BindView(R.id.chat_dot)
        CircleImageView chat_dot;

        @BindView(R.id.report)
        LinearLayout report;

        @BindView(R.id.num_post)
        TextView num_post;

        @BindView(R.id.report_image)
        ImageView report_image;

        @BindView(R.id.rL_tweet)
        RelativeLayout rl_tweet;

        @BindView(R.id.image_user_post)
        CircleImageView user_img;

        @BindView(R.id.poster_name)
        TextView poster_name;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.report_dot)
        CircleImageView report_dot;


        public ShoutsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
*/
