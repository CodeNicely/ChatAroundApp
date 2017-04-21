package com.fame.plumbum.chataround.news.presenter;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsListPresenter {
    void getNews(
            boolean cache, String userId, String city, String state, String country
    );

    void onDestroy();

}
