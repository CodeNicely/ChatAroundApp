package com.fame.plumbum.chataround.restroom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by meghal on 6/3/17.
 */

public class RestroomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RestRoomDetails> restRoomDetailsList = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    RestroomAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new GlideImageLoader(context);
    }

    void setData(List<RestRoomDetails> restRoomDetailsList) {
        this.restRoomDetailsList = restRoomDetailsList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.restroom_item, parent, false);
        return new RestroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final RestRoomDetails restRoomDetails = restRoomDetailsList.get(position);
        RestroomViewHolder restroomViewHolder = (RestroomViewHolder) holder;

        restroomViewHolder.viewPager.setAdapter(restroomViewHolder.imagesAdapter);

        restroomViewHolder.imagesAdapter.setData(restRoomDetails.getImage_list());

        restroomViewHolder.imagesAdapter.notifyDataSetChanged();

        restroomViewHolder.address.setText(restRoomDetails.getAddress());
        restroomViewHolder.distance.setText(String.valueOf(restRoomDetails.getDistance()) + " meters");

        if (restRoomDetails.isMale()) {
            restroomViewHolder.male.setVisibility(View.VISIBLE);
            restroomViewHolder.male.setImageResource(R.drawable.man_standing);
        } else {
            restroomViewHolder.male.setVisibility(View.GONE);

        }

        if (restRoomDetails.isFemale()) {
            restroomViewHolder.female.setVisibility(View.VISIBLE);
            restroomViewHolder.female.setImageResource(R.drawable.woman_standing);

        } else {
            restroomViewHolder.female.setVisibility(View.GONE);
        }

        if (restRoomDetails.isDisabled()) {
            restroomViewHolder.disabled.setVisibility(View.VISIBLE);
            restroomViewHolder.disabled.setImageResource(R.drawable.disabled);


        } else {
            restroomViewHolder.disabled.setVisibility(View.GONE);

        }


        restroomViewHolder.owner.setText("Restroom added by " + restRoomDetails.getUsername());

        restroomViewHolder.getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {

                    ((MainActivity) context).openGoogleMaps(
                            restRoomDetails.getLatitude(),
                            restRoomDetails.getLongitude()
                    );
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return restRoomDetailsList.size();
    }

    public class RestroomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.distance)
        TextView distance;

        @BindView(R.id.address)
        TextView address;

        @BindView(R.id.male)
        ImageView male;

        @BindView(R.id.female)
        ImageView female;

        @BindView(R.id.disabled)
        ImageView disabled;

        @BindView(R.id.viewpager)
        ViewPager viewPager;


        @BindView(R.id.owner)
        TextView owner;

        @BindView(R.id.getRoute)
        Button getRoute;

        ImagesAdapter imagesAdapter;

        public RestroomViewHolder(View itemView) {
            super(itemView);
            imagesAdapter = new ImagesAdapter(context);
            ButterKnife.bind(this, itemView);
        }
    }
}
