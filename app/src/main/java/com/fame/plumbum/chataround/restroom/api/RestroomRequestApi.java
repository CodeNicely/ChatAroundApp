package com.fame.plumbum.chataround.restroom.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghal on 20/2/17.
 */

public interface RestroomRequestApi {

    @GET(Urls.SUB_URL_RESTROOM)
    Call<List<RestRoomData>> requestRestRooms(
            @Query("lat") Double latitude,
            @Query("lng") Double langitude
    );


}
