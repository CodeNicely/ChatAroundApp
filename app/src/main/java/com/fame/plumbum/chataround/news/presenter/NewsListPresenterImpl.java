package com.fame.plumbum.chataround.news.presenter;

import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;
import com.fame.plumbum.chataround.news.model.NewsListProvider;
import com.fame.plumbum.chataround.news.model.data.NewsListData;
import com.fame.plumbum.chataround.news.view.NewsFragment;

import retrofit2.Retrofit;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListPresenterImpl implements NewsListPresenter {
    private NewsFragment newsFragment;
    private NewsListProvider newsListProvider;

    public NewsListPresenterImpl(NewsFragment newsFragment, NewsListProvider newsListProvider) {
        this.newsFragment = newsFragment;
        this.newsListProvider = newsListProvider;
    }


    @Override
    public void getNews(String  userId, String city) {
        newsFragment.showProgressBar(true);
        newsListProvider.getNewsList(userId, city, new NewsFeedRequestCallback() {
            @Override
            public void onSuccess(NewsListData newsListData) {
                newsFragment.showProgressBar(false);
                if (newsListData.isSuccess())
                {
                    newsFragment.setNewsList(newsListData.getNewsListDataDetailsList());
                }
                else
                {
                   newsFragment.showMessage(newsListData.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                newsFragment.showMessage("something went wrong please try again!");
            }
        });

    }
}
