package com.fame.plumbum.chataround.news.provider;

import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsListProvider {
    void getNewsList(
            boolean cache,String userId,
                     String city,
                     String state,
                     String country,
                     NewsFeedRequestCallback newsFeedRequestCallback);
}
