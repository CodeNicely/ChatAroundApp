package com.fame.plumbum.chataround.news.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.news.model.data.NewsListDataDetails;

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
    private NewsFragment newsFragment;
    private NewsListDataDetails newsListDataDetails;
    private ImageLoader imageLoader;
    private List<NewsListDataDetails> newsListDataDetailsList= new ArrayList<>();
    public NewsListAdapter(Context context,NewsFragment newsFragment)
    {
        this.context=context;
        this.newsFragment=newsFragment;
        this.layoutInflater=LayoutInflater.from(context);
        this.imageLoader=new GlideImageLoader(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.news_list_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        newsListDataDetails=newsListDataDetailsList.get(position);
        final NewsViewHolder newsViewHolder=(NewsViewHolder)holder;
        newsViewHolder.newsTitle.setText(newsListDataDetails.getTitle());
        if(newsListDataDetails.getImage()!=null)
        {
            newsViewHolder.news_relative_image_layout.setVisibility(View.VISIBLE);
            imageLoader.loadImage(newsListDataDetails.getImage(),newsViewHolder.newsImage,newsViewHolder.imageProgressBar);
        }
        newsViewHolder.news_published_timestamp.setText(newsListDataDetails.getPublished_at());
        newsViewHolder.newsAuthor.setText(newsListDataDetails.getAuthor());
        newsViewHolder.newsDescription.setText(newsListDataDetails.getBody());
        newsViewHolder.newsSource.setText(newsListDataDetails.getSource());
        newsViewHolder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        @BindView(R.id.newsImage)
        ImageView newsImage;
        @BindView(R.id.newsTitle)
        TextView newsTitle;
        @BindView(R.id.newsAuthor)
        TextView newsAuthor;
        @BindView(R.id.newSource)
        TextView newsSource;
        @BindView(R.id.NewsDescription)
        TextView newsDescription;
        @BindView(R.id.timestamp)
        TextView news_published_timestamp;
        @BindView(R.id.imageProgressBar)
        ProgressBar imageProgressBar;
        @BindView(R.id.news_card)
        CardView product_card;
        @BindView(R.id.newsLayout)
        RelativeLayout news_relative_image_layout;
        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
