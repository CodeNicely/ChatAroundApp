package com.fame.plumbum.chataround.add_restroom.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.add_restroom.model.data.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends Adapter and it acts as adapter fot Viewing selected or clicked images in a
 * Recycler View {@link UploadImageView} View.
 * <p>
 * Created by Meghal on 5/26/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private static final String TAG = "ImageAdapter";
    private LayoutInflater layoutInflater;
    private Context context;
    private List<ImageData> imageDataList = new ArrayList<>();
    private ImageLoader imageLoader;

    ImageAdapter(Context context) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new GlideImageLoader(context);

    }

    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.image_upload_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.MyViewHolder holder, int position) {
/*
//        This is used for animation from xml up and down anim // #Meghal dont delete the comment
        animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up
                        : R.anim.down);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
*/
        ImageData imageData = imageDataList.get(position);
        imageLoader.loadImage(imageData.getFile(), holder.imageView,null);

    }

    @Override
    public int getItemCount() {

        //    Log.i(TAG, "Data Size :" + imageDataList.size());
        return imageDataList.size();
    }

    public void setData(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.data);
        }
    }

}
