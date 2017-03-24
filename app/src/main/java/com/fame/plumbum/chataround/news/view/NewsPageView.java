package com.fame.plumbum.chataround.news.view;

import com.fame.plumbum.chataround.news.model.NewsListDataDetails;

import java.util.List;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsPageView {
    void showProgressBar(boolean show);
    void showMessage(String message);
    void setNewsList(List<NewsListDataDetails> newsListDataDetailsList);
}
