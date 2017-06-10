package com.fame.plumbum.chataround.restroom.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meghal on 6/3/17.
 */

public class ImagesAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imageUrl = new ArrayList<>();
    private ImageLoader imageLoader;


    ImagesAdapter(Context context) {

        this.context = context;
        imageLoader = new GlideImageLoader(context);

    }

    void setData(ArrayList<String> imageUrl) {

        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        String url = imageUrl.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_image, container, false);
        container.addView(view);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imageLoader.loadImage(Urls.BASE_URL + "ImageReturn?ImageName=" + url, imageView, progressBar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).openImageViewer(imageUrl, position);
                    Answers.getInstance().logCustom(new CustomEvent("Restroom photos clicked"));

                }
            }
        });

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
        return 0.75f;
    }
}
