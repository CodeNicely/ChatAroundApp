package com.fame.plumbum.chataround.add_restroom.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.add_restroom.model.data.ImageData;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends Adapter and it acts as adapter fot Viewing selected or clicked images in a
 * Recycler View {@link UploadImageView} View.
 * <p>
 * Created by Meghal on 5/26/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ImageAdapter";
    private static final int VIEW_TYPE_IMAGE = 100;
    private static final int VIEW_TYPE_FOOTER = 200;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_FOOTER) {

            view = layoutInflater.inflate(R.layout.image_footer, parent, false);
            return new FooterViewHolder(view);
        } else {

            view = layoutInflater.inflate(R.layout.image_upload_item, parent, false);

            return new ImageViewHolder(view);

        }
    }


    @Override
    public int getItemViewType(int position) {

        if (imageDataList.get(position).isFooter()) {

            return VIEW_TYPE_FOOTER;
        } else {

            return VIEW_TYPE_IMAGE;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
/*
//        This is used for animation from xml up and down anim // #Meghal dont delete the comment
        animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up
                        : R.anim.down);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
*/
        if (imageDataList.get(position).isFooter()) {

            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof AddRestroomActivity) {
                        ((AddRestroomActivity) context).openCamera();
                    }
                }
            });

        } else {

            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            ImageData imageData = imageDataList.get(position);
            imageLoader.loadImage(imageData.getFile(), imageViewHolder.imageView, null);
        }
    }

    @Override
    public int getItemCount() {

        //    Log.i(TAG, "Data Size :" + imageDataList.size());
        return imageDataList.size();
    }

    public void setData(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    protected class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.data);
        }
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private LinearLayout linearLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.add_images);
            imageView = (ImageView) itemView.findViewById(R.id.cameraButton);
        }
    }



}
