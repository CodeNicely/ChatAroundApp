package com.fame.plumbum.chataround.restroom.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meghal on 6/3/17.
 */

public class ImagesAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageUrl = new ArrayList<>();
    private ImageLoader imageLoader;


    ImagesAdapter(Context context) {

        this.context=context;
        imageLoader = new GlideImageLoader(context);

    }

    void setData(List<String> imageUrl) {

        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String url = imageUrl.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_image, container, false);
        container.addView(view);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imageLoader.loadImage(url, imageView, progressBar);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public float getPageWidth(int position) {
        super.getPageWidth(position);
        return 0.4f;
    }
}
