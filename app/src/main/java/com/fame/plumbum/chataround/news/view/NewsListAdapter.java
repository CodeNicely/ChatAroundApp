package com.fame.plumbum.chataround.news.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        newsViewHolder.newsTitle.setText(newsListDataDetails.getTitle()+".....");
        if (newsListDataDetails.getImage() != null) {

            newsViewHolder.imageView.setVisibility(View.VISIBLE);
            imageLoader.loadImage(newsListDataDetails.getImage(), newsViewHolder.imageView, newsViewHolder.progressBar);
        } else {
            newsViewHolder.imageView.setVisibility(View.GONE);
            newsViewHolder.progressBar.setVisibility(View.GONE);
        }

//        if (newsListDataDetails.getSource() == null) {
//            newsViewHolder.newsSource.setVisibility(View.GONE);
//        } else {
//            newsViewHolder.newsSource.setText(newsListDataDetails.getSource());
//
//            newsViewHolder.newsSource.setVisibility(View.VISIBLE);
//        }


        if (newsListDataDetails.getAuthor() == null) {
            newsViewHolder.newsAuthor.setVisibility(View.GONE);
        } else {
            newsViewHolder.newsAuthor.setText(newsListDataDetails.getAuthor());

            newsViewHolder.newsAuthor.setVisibility(View.VISIBLE);
        }
//        newsViewHolder.newsDescription.setText(newsListDataDetails.getBody_small());
//        newsViewHolder.news_published_timestamp.setText(newsListDataDetails.getPublished_at());
//        newsViewHolder.newsAuthor.setText(newsListDataDetails.getAuthor());
        newsViewHolder.news_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    NewsListDataDetails newsListDataDetails=newsListDataDetailsList.get(position);
                    ((MainActivity) context).openNewsDetails(
                            newsListDataDetails.getTitle(),
                            newsListDataDetails.getImage(),
                            newsListDataDetails.getSource(),
                            newsListDataDetails.getBody(),
                            newsListDataDetails.getAuthor(),
                            newsListDataDetails.getPublished_at());
                }
            }
        });

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
        @BindView(R.id.NewsSmallDescription)
        TextView newsDescription;
        @BindView(R.id.timestamp)
        TextView news_published_timestamp;
        @BindView(R.id.news_card)
        CardView news_card;

        @BindView(R.id.image)
        ImageView imageView;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
