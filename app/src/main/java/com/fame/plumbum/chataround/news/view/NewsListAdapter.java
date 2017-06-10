package com.fame.plumbum.chataround.news.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.news.model.NewsListDataDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private NewsListFragment newsListFragment;
    private NewsListDataDetails newsListDataDetails;
    private ImageLoader imageLoader;
    private List<NewsListDataDetails> newsListDataDetailsList = new ArrayList<>();

    public NewsListAdapter(Context context, NewsListFragment newsListFragment) {
        this.context = context;
        this.newsListFragment = newsListFragment;
        this.layoutInflater = LayoutInflater.from(context);
        this.imageLoader = new GlideImageLoader(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        newsListDataDetails = newsListDataDetailsList.get(position);

        final NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

        newsViewHolder.newsTitle.setText(newsListDataDetails.getTitle());


        if (newsListDataDetails.getImage() != null && !newsListDataDetails.getImage().equals("")) {


            Glide.with(context)
                    .load(newsListDataDetails.getImage())
                    .crossFade()
                    .thumbnail(0.05f)
                    .load(newsListDataDetails.getImage())
                    .crossFade()
                    .thumbnail(0.1f)
                    .load(newsListDataDetails.getImage())
                    .crossFade()
                    .thumbnail(1f)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            Glide.with(context)
                                    .load(R.drawable.world_black)
                                    .crossFade()
                                    .thumbnail(0.05f)
                                    .load(R.drawable.world_black)
                                    .crossFade()
                                    .thumbnail(0.1f)
                                    .load(R.drawable.world_black)
                                    .crossFade()
                                    .thumbnail(1f).into(newsViewHolder.placeHolder);
                            newsViewHolder.progressBar.setVisibility(View.GONE);
                            newsViewHolder.imageView.setVisibility(View.GONE);
                            newsViewHolder.placeHolder.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            newsViewHolder.placeHolder.setVisibility(View.GONE);
                            newsViewHolder.progressBar.setVisibility(View.GONE);
                            newsViewHolder.imageView.setVisibility(View.VISIBLE);

                            return false;
                        }
                    })
                    .into(newsViewHolder.imageView);

            newsViewHolder.imageView.setVisibility(View.VISIBLE);
            newsViewHolder.progressBar.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(R.drawable.world_black)
                    .crossFade()
                    .thumbnail(0.05f)
                    .load(R.drawable.world_black)
                    .crossFade()
                    .thumbnail(0.1f)
                    .load(R.drawable.world_black)
                    .crossFade()
                    .thumbnail(1f).into(newsViewHolder.placeHolder);
            newsViewHolder.progressBar.setVisibility(View.GONE);
            newsViewHolder.imageView.setVisibility(View.GONE);
            newsViewHolder.placeHolder.setVisibility(View.VISIBLE);
        }


        if (newsListDataDetails.getAuthor() == null) {
            newsViewHolder.newsAuthor.setVisibility(View.GONE);
        } else {
            newsViewHolder.newsAuthor.setText(newsListDataDetails.getAuthor());

            newsViewHolder.newsAuthor.setVisibility(View.VISIBLE);
            newsViewHolder.news_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof MainActivity) {
                        NewsListDataDetails newsListDataDetails = newsListDataDetailsList.get(position);
                        ((MainActivity) context).openNewsDetails(
                                newsListDataDetails.getTitle(),
                                newsListDataDetails.getImage(),
                                newsListDataDetails.getSource(),
                                newsListDataDetails.getBody(),
                                newsListDataDetails.getAuthor(),
                                newsListDataDetails.getPublished_at(),
                                newsListDataDetails.getUrl()
                        );
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return newsListDataDetailsList.size();
    }

    public void setNewsListDataDetailsList(List<NewsListDataDetails> newsListDataDetailsList) {
        this.newsListDataDetailsList = newsListDataDetailsList;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newsTitle)
        TextView newsTitle;

        @BindView(R.id.newsAuthor)
        TextView newsAuthor;

        @BindView(R.id.newSource)
        TextView newsSource;

//        @BindView(R.id.NewsSmallDescription)
//        TextView newsDescription;

        @BindView(R.id.timestamp)
        TextView news_published_timestamp;

        @BindView(R.id.news_card)
        CardView news_card;

        @BindView(R.id.image)
        ImageView imageView;

        @BindView(R.id.placeHolder)
        ImageView placeHolder;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
