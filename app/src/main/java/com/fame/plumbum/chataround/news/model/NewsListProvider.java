package com.fame.plumbum.chataround.news.model;

import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsListProvider {
    void getNewsList(String userId,
                     String city,
                     String state,
                     String country,
                     NewsFeedRequestCallback newsFeedRequestCallback);
}
