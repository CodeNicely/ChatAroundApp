package com.fame.plumbum.chataround.news.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.model.NewsListData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ramya on 10/3/17.
 */

public interface NewsListRequestApi {
    @GET(Urls.SUB_URL_NEWS_LIST)
    Call<NewsListData> getNewsList(@Query("userId") String userId,
                                   @Query("city") String city,
                                   @Query("state") String state,
                                   @Query("country") String country

    );

}
