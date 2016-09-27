package com.fame.plumbum.chataround.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.ParticularPost;
import com.fame.plumbum.chataround.database.DBHandler;
import com.fame.plumbum.chataround.database.NotifTable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 19/8/16.
 */
public class Notifs extends BaseAdapter {
    private Context context;
    public JSONArray posts;
    public int total = 0;
    DBHandler db;
    Typeface typeFace, typeface_light;

    public Notifs(Context context, JSONArray posts){
        this.context = context;
        this.posts = posts;
        total = posts.length();
        db = new DBHandler(context);
        typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_thin.ttf");
        typeface_light = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_light.ttf");
    }
    @Override
    public int getCount() {
        return total;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.notifs, parent, false);
        RelativeLayout rl_tweet = (RelativeLayout) rowView.findViewById(R.id.rL_tweet);
        final TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView poster_name = (TextView) rowView.findViewById(R.id.poster_name);
        message.setTypeface(typeFace);
        poster_name.setTypeface(typeface_light);
        CircleImageView report_dot = (CircleImageView) rowView.findViewById(R.id.report_dot);
        try {
            String NReports = posts.getJSONObject(position).getString("NLike");
            String NComments = posts.getJSONObject(position).getString("NComment");
            poster_name.setText(posts.getJSONObject(position).getString("Post"));
            if (NReports.contentEquals("0")){
                message.setText(NComments + " new comments on a post");
            }else if (NComments.contentEquals("0")){
                message.setText(NReports + " more people reported a post.");
            }else{
                message.setText(NComments + " new comments\n"+ NReports + " new reports");
            }
            if (!posts.getJSONObject(position).getString("NoOfLikes").contentEquals("0")){
                report_dot.setVisibility(View.VISIBLE);
            }else{
                report_dot.setVisibility(View.GONE);
            }
        } catch (JSONException ignored) {
        }
        rl_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                Log.e("Clicked", "RLEATIVE LATOUT");
                try {
                    DBHandler db = new DBHandler(context);
                    db.deleteNotif(posts.getJSONObject(position).getString("PostId"));
                    Log.e("Posts", posts.getJSONObject(position).getString("PostId"));
                    List<NotifTable> notifTables = db.getNotifs();
                    Log.e("SIZEINADAPTER", notifTables.size()+"");
                    Intent intent = new Intent(context, ParticularPost.class);
                    intent.putExtra("post_id", posts.getJSONObject(position).getString("PostId"));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
        return rowView;
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
