package com.fame.plumbum.chataround.referral.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.referral.model.ReferalData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by meghalagrawal on 26/07/17.
 */

public interface VerifyReferralApi {

    @FormUrlEncoded
    @POST(Urls.SUB_URL_VERIFY_REFERRAL)
    Call<ReferalData> requestVerifyReferral(
            @Field("userId") String userId,
            @Field("deviceId") String deviceId,
            @Field("mobile") String mobile,
            @Field("otp") String otp
    );

}
