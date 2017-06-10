package com.fame.plumbum.chataround.shouts.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.model.NewsListData;
import com.fame.plumbum.chataround.shouts.model.ShoutsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public interface ShoutsApi {

    @GET(Urls.SUB_URL_SHOUTS)
    Call<ShoutsData> requestShouts(@Query("UserId") String userId,
                               @Query("Counter") int counter,
                               @Query("Latitude") double latitude,
                               @Query("Longitude") double longitude

    );
}
