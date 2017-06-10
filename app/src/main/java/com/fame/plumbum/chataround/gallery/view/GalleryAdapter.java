package com.fame.plumbum.chataround.gallery.view;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.gallery.model.ImageData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ImageData> imageDataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private List<ImageData> data;

    public GalleryAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        imageLoader=new GlideImageLoader(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ImageData imageData=imageDataList.get(position);

        GalleryViewHolder galleryViewHolder=(GalleryViewHolder)holder;

        imageLoader.loadImage(Urls.BASE_URL + "ImageReturn?ImageName=" + imageData.getImage_url(),galleryViewHolder.imageView,galleryViewHolder.progressBar);
        galleryViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answers.getInstance().logCustom(new CustomEvent("Gallery photos clicked"));
                ((MainActivity) context).openImageViewer(imageData.getImage_url(), position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public void setData(List<ImageData> data) {
        this.imageDataList = data;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public GalleryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
