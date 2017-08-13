package com.fame.plumbum.chataround.shouts.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.MySingleton;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.ParticularChat;
import com.fame.plumbum.chataround.activity.ParticularPost;
import com.fame.plumbum.chataround.activity.SelfChatList;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.shouts.model.Posts;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public class ShoutsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VOTE_TYPE_UPVOTE = 1;
    private static final int VOTE_TYPE_DOWNVOTE = -1;
    private static final String TAG = "ShoutsRecyclerAdapter";

    private LayoutInflater layoutInflater;
    private List<Posts> postsList = new ArrayList<>();
    private ShoutsFragment shoutsFragment;
    private Context context;
    private DBHandler db;

    public ShoutsRecyclerAdapter(ShoutsFragment shoutsFragment, Context context) {
        db = new DBHandler(context);
        layoutInflater = LayoutInflater.from(context);
        this.shoutsFragment = shoutsFragment;
        this.context = context;
    }

    public void setData(List<Posts> postsList) {
        this.postsList = postsList;
    }

    public void appendData(List<Posts> postsList) {
        this.postsList.addAll(postsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.shouts_item_new_new, parent, false);
        return new ShoutsViewHolder(view);
    }


    public List<Posts> getPostsList() {
        return postsList;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Posts post = postsList.get(position);

        final ShoutsViewHolder shoutsViewHolder = (ShoutsViewHolder) holder;

        shoutsViewHolder.user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shoutsFragment.showProfileDialog(post.getPosterName()
                        , Urls.BASE_URL + "ImageReturn?ImageName=" + post.getPosterImage());

            }
        });

        Glide.with(context).load(R.drawable.chat).into(shoutsViewHolder.comment_image);
        Glide.with(context).load(R.drawable.reply).into(shoutsViewHolder.chat_image);

        if (db.getPeronalChats(post.getPosterId()).size() > 0)
            shoutsViewHolder.chat_dot.setVisibility(View.VISIBLE);
        else
            shoutsViewHolder.chat_dot.setVisibility(View.GONE);
        int reports = post.getLike().size();
        int comments = post.getComments().size();

        shoutsViewHolder.number_of_comments.setText(String.valueOf(comments+" Comments"));

      /*  if (reports == 0 && comments == 0) {
            shoutsViewHolder.num_post.setText("No entries found.");



        } else if (reports == 0) {
            shoutsViewHolder.num_post.setText(comments + " comments");
        } else if (comments == 0) {
            shoutsViewHolder.num_post.setText(reports + " reports");
        } else {



            shoutsViewHolder.num_post.setText(comments + " comments and " + reports + " reports");
        }*/
/*
        if (post.getNoOfLikes() != 0) {
            shoutsViewHolder.report_dot.setVisibility(View.VISIBLE);
        } else {
            shoutsViewHolder.report_dot.setVisibility(View.GONE);
        }
*/

//        shoutsViewHolder.num_post.setVisibility(View.VISIBLE);

/*
        if (post.getLikeFlag().contentEquals("0"))
            Glide.with(context).load(R.drawable.thumbs_down_accent).into(shoutsViewHolder.report_image);
        else
            Glide.with(context).load(R.drawable.thumbs_down_red).into(shoutsViewHolder.report_image);
*/

        Glide.with(context).load(R.drawable.downvote).into(shoutsViewHolder.downvote_image);

//        Glide.with(context).load(R.drawable.timestamp).into(shoutsViewHolder.timestamp_icon);

        try {
            shoutsViewHolder.upvotes.setText(String.valueOf(post.getNUpvote()));

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }

        try {
            shoutsViewHolder.downvotes.setText(String.valueOf(post.getNDownvote()));
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());

        }

 /*       try{
            if(post.isUpVoteFlag() || post.isDownVoteFlag()){

                shoutsViewHolder.voteLayout.setVisibility(View.GONE);
                shoutsViewHolder.voteTextLayout.setVisibility(View.VISIBLE);

               // User has already voted for this post so the user can not vote any more.

            }else{
                shoutsViewHolder.voteLayout.setVisibility(View.VISIBLE);
                shoutsViewHolder.voteTextLayout.setVisibility(View.GONE);

            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

        try{
            if(post.isUpVoteFlag()){

                shoutsViewHolder.upvote_image.setAlpha(Float.parseFloat("1"));

                Glide.with(context).load(R.drawable.up_vote).into(shoutsViewHolder.upvote_image);

            }else{
                shoutsViewHolder.upvote_image.setAlpha(Float.parseFloat("0.3"));

                Glide.with(context).load(R.drawable.up_vote).into(shoutsViewHolder.upvote_image);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        try{

            if(post.isDownVoteFlag()){
                shoutsViewHolder.downvote_image.setAlpha(Float.parseFloat("1"));
                Glide.with(context).load(R.drawable.down_vote).into(shoutsViewHolder.downvote_image);

            }else{
                shoutsViewHolder.downvote_image.setAlpha(Float.parseFloat("0.3"));

                Glide.with(context).load(R.drawable.down_vote).into(shoutsViewHolder.downvote_image);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        Glide.with(context).load(Urls.BASE_URL + "ImageReturn?ImageName=" + postsList.get(position).getPosterImage()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Glide.with(context).load(R.drawable.user_big).into(shoutsViewHolder.user_img);
                return true;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(shoutsViewHolder.user_img);

        shoutsViewHolder.poster_name.setText(toProperCase(post.getPosterName().replace("%20", " ")));

        shoutsViewHolder.poster_name.setText(post.getPosterName().replace("%20", " "));

        shoutsViewHolder.message.setText(post.getPost().replace("%20", " "));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = sdf.parse(post.getTimeStamp());

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
            /*if (post.getNoOfLikes() != 0) {
                shoutsViewHolder.report_dot.setVisibility(View.VISIBLE);
            } else {
                shoutsViewHolder.report_dot.setVisibility(View.GONE);
            }*/
        } catch (ParseException e) {
            e.printStackTrace();
        }

/*        shoutsViewHolder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shoutsFragment.changeReportStatus(postsList.get(position).getPostId(), position);


            }
        });*/
        shoutsViewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                if (!post.getPosterId().contentEquals(sp.getString("uid", ""))) {
                    final Intent intent = new Intent(context, ParticularChat.class);
                    intent.putExtra("post_id", post.getPosterId());
                    intent.putExtra("uid_r", post.getPosterId());
                    intent.putExtra("poster_name", post.getPosterId().replace("%20", " "));
                    intent.putExtra("remote_name", post.getPosterId().replace("%20", " "));
                    context.startActivity(intent);
                    Answers.getInstance().logCustom(new CustomEvent("Shout chat clicked"));

                } else {
                    Intent intent = new Intent(context, SelfChatList.class);
                    intent.putExtra("post_id", post.getPostId());
                    context.startActivity(intent);
                    Answers.getInstance().logCustom(new CustomEvent("Shout chat clicked"));

                }

            }
        });



        shoutsViewHolder.rl_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answers.getInstance().logCustom(new CustomEvent("Shout Opened/ Shout comment clicked"));

                Intent intent = new Intent(context, ParticularPost.class);
                intent.putExtra("post_id", post.getPostId());
                context.startActivity(intent);
            }
        });


        shoutsViewHolder.upvote_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shoutsFragment.requestVote(post.getPostId(),VOTE_TYPE_UPVOTE,position);

            }
        });

        shoutsViewHolder.downvote_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoutsFragment.requestVote(post.getPostId(),VOTE_TYPE_DOWNVOTE,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postsList.size();
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

    public void setItem(int position, Posts post) {
        postsList.set(position,post);
    }


    public class ShoutsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat)
        LinearLayout chat;

//        @BindView(R.id.report)
//        LinearLayout report;

//        @BindView(R.id.num_post)
//        TextView num_post;

        @BindView(R.id.rL_tweet)
        RelativeLayout rl_tweet;

        @BindView(R.id.poster_name)
        TextView poster_name;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.timestamp)
        TextView timestamp;

//        @BindView(R.id.report_dot)
//        CircleImageView report_dot;

        @BindView(R.id.image_user_post)
        CircleImageView user_img;

        @BindView(R.id.chat_dot)
        ImageView chat_dot;

//        @BindView(R.id.report_image)
//        ImageView report_image;

        @BindView(R.id.chat_image)
        ImageView chat_image;

        @BindView(R.id.comment_image)
        ImageView comment_image;

        @BindView(R.id.upvote)
        ImageView upvote_image;

        @BindView(R.id.downvote)
        ImageView downvote_image;

        @BindView(R.id.upvotes)
        TextView upvotes;

        @BindView(R.id.downvotes)
        TextView downvotes;

        @BindView(R.id.voteLayout)
        LinearLayout voteLayout;

        @BindView(R.id.voteTextLayout)
        LinearLayout voteTextLayout;

//        @BindView(R.id.timestamp_icon)
//        ImageView timestamp_icon;

        @BindView(R.id.number_of_replies)
        TextView number_of_replies;

        @BindView(R.id.number_of_comment)
        TextView number_of_comments;

        public ShoutsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


}
