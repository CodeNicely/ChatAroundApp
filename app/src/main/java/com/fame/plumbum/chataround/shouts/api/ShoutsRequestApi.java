package com.fame.plumbum.chataround.shouts.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.shouts.data.ShoutData1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghal on 20/2/17.
 */

public interface ShoutsRequestApi {

    @GET(Urls.SUB_URL_SHOUTS)
    Call<ShoutData1> requestShouts(
            @Query("UserId") String UserId,
            @Query("Counter") int Counter,
            @Query("Latitude") double latitude,
            @Query("Longitude") double langitude
    );


}
