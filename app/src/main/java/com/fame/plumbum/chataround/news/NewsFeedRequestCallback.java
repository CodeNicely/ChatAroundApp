package com.fame.plumbum.chataround.news;

import com.fame.plumbum.chataround.news.model.NewsListData;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsFeedRequestCallback {
    void onSuccess(NewsListData newsListData);
    void OnFailure(String message);
}
