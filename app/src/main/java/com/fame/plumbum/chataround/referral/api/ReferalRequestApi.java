package com.fame.plumbum.chataround.referral.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.referral.model.ReferalData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by aman on 18/5/17.
 */

public interface ReferalRequestApi {

    @FormUrlEncoded
    @POST(Urls.SUB_URL_ADD_REFERRAL)
    Call<ReferalData> requestPaytmCash(
            @Field("userId") String userId,
            @Field("deviceId") String deviceId,
            @Field("mobile") String mobile);

}