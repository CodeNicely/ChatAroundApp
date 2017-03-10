package com.fame.plumbum.chataround.news.model.data;

import java.util.List;

/**
 * Created by ramya on 10/3/17.
 */

public class NewsListData {
    private String message;
    private boolean success;
    private List<NewsListDataDetails> newsListDataDetailsList;

    public NewsListData(String message, boolean success, List<NewsListDataDetails> newsListDataDetailsList) {
        this.message = message;
        this.success = success;
        this.newsListDataDetailsList = newsListDataDetailsList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<NewsListDataDetails> getNewsListDataDetailsList() {
        return newsListDataDetailsList;
    }

    public void setNewsListDataDetailsList(List<NewsListDataDetails> newsListDataDetailsList) {
        this.newsListDataDetailsList = newsListDataDetailsList;
    }
}
