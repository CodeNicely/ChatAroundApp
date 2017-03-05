package com.fame.plumbum.chataround.restroom.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;
import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghal on 20/2/17.
 */

public interface RestroomRequestApi {

    @GET(Urls.SUB_URL_RESTROOM)
    Call<RestRoomData> requestRestRooms(
            @Query("user_id") String user_id,
            @Query("latitude") double latitude,
            @Query("longitude") double langitude
    );


}
