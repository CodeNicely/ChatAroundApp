package com.fame.plumbum.chataround.news.presenter;

import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;
import com.fame.plumbum.chataround.news.provider.NewsListProvider;
import com.fame.plumbum.chataround.news.model.NewsListData;
import com.fame.plumbum.chataround.news.view.NewsListFragment;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListPresenterImpl implements NewsListPresenter {
    private NewsListFragment newsListFragment;
    private NewsListProvider newsListProvider;

    public NewsListPresenterImpl(NewsListFragment newsListFragment, NewsListProvider newsListProvider) {
        this.newsListFragment = newsListFragment;
        this.newsListProvider = newsListProvider;
    }


    @Override
    public void getNews(boolean cache,String userId, String city,String state,String country) {
        newsListFragment.showProgressBar(true);
        newsListProvider.getNewsList(cache,userId, city,state,country, new NewsFeedRequestCallback() {
            @Override
            public void onSuccess(NewsListData newsListData) {
                newsListFragment.showProgressBar(false);
                if (newsListData.isSuccess()) {
                    newsListFragment.setNewsList(newsListData.getNewsListDataDetailsList());
                } else {
                    newsListFragment.showMessage(newsListData.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                newsListFragment.showMessage("something went wrong please try again!");
            }
        });

    }
}
