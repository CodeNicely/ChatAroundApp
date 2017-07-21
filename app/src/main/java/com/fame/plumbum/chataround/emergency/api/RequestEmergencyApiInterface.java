package com.fame.plumbum.chataround.emergency.api;

import com.fame.plumbum.chataround.emergency.model.EmergencyContactDeleteData;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsListData;
import com.fame.plumbum.chataround.emergency.model.StartSosData;
import com.fame.plumbum.chataround.emergency.model.UpdateSosData;
import com.fame.plumbum.chataround.helper.Urls;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ramya on 5/7/17.
 */

public interface RequestEmergencyApiInterface {
    @GET(Urls.SUB_URL_GET_EMERGENCY_CONTACTS)
    Call<EmergencyContactsListData> getContactsList(@Query("userId") String userId);

    @FormUrlEncoded
    @POST(Urls.SUB_URL_DELETE_EMERGENCY_CONTACTS)
    Call<EmergencyContactDeleteData> deleteContact(@Field("userId") String userId, @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(Urls.SUB_URL_START_SOS)
    Call<StartSosData> startSos(@Field("userId") String userId,
                                @Field("latitude") double latitude,
                                @Field("longitude") double longitude

    );

    @FormUrlEncoded
    @POST(Urls.SUB_URL_UPDATE_SOS_LOCATION)
    Call<UpdateSosData> updateSosData(
            @Field("userId") String userId,
            @Field("sosId") String sosId,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );


}
