package com.fame.plumbum.chataround.add_restroom.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddRestroomApi {

    @FormUrlEncoded
    @POST(Urls.SUB_URL_ADD_RESTROOM)
    Call<AddRestroomData> addRestroom(
            @Field("username") String username,
            @Field("userMobile") String userMobile,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("address") String address,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("postalCode") String postalCode,
            @Field("knownName") String knownName,
            @Field("male") boolean male,
            @Field("female") boolean female,
            @Field("disabled") boolean disabled,
            @Field("mobile")String mobile

    );


}
