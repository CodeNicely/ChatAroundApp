package com.fame.plumbum.chataround.news.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.timestamp)
    TextView news_timestamp;

    @BindView(R.id.NewsDescription)
    TextView news_description;

    @BindView(R.id.newSource)
    TextView news_source;

    @BindView(R.id.image)
    ImageView news_image;

    @BindView(R.id.newsAuthor)
    TextView news_author;

    @BindView(R.id.newsTitle)
    TextView news_title;

    @BindView(R.id.progressBar)
    ProgressBar imageProgressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



        ImageLoader imageLoader = new GlideImageLoader(this);

        if (getIntent() != null) {

            Bundle bundle = getIntent().getExtras();

            String source = bundle.getString(Keys.NEWS_SOURCE);
            String author = bundle.getString(Keys.NEWS_AUTHOR);
            String description = bundle.getString(Keys.NEWS_DESCRIPTION);
            String image_url = bundle.getString(Keys.NEWS_IMAGE);
            String title = bundle.getString(Keys.NEWS_TITLE);
            String timestamp = bundle.getString(Keys.NEWS_TIMESTAMP);


            if (image_url != null) {
                news_image.setVisibility(View.VISIBLE);
                imageLoader.loadImage(image_url, news_image, imageProgressBar);
            } else {
                news_image.setVisibility(View.GONE);
                imageProgressBar.setVisibility(View.GONE);
            }
            if (source != null) {
                news_source.setVisibility(View.VISIBLE);
                news_source.setText(source);
            } else {
                news_source.setVisibility(View.GONE);
            }
            if (author != null) {
                news_author.setVisibility(View.VISIBLE);
                news_author.setText(author);
            } else {
                news_author.setVisibility(View.GONE);
            }

            if (description != null) {
                news_description.setVisibility(View.VISIBLE);
                news_description.setText(description);
            } else {
                news_description.setVisibility(View.GONE);
            }

            if (title != null) {
                news_title.setVisibility(View.VISIBLE);
                news_title.setText(title);
                toolbar.setTitle(title);
            } else {
                news_title.setVisibility(View.GONE);
            }
            if (timestamp != null) {
                news_timestamp.setVisibility(View.VISIBLE);
                news_timestamp.setText(timestamp);
            } else {
                news_timestamp.setVisibility(View.GONE);
            }

        }
    }
}
