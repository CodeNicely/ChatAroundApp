package com.fame.plumbum.chataround.referral.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.referral.OnDeviceVerifyResponse;
import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface VerifyDeviceApi {


    @FormUrlEncoded
    @POST(Urls.SUB_URL_VERIFY_DEVICE)
    Call<VerifyDeviceData> requestDeviceVerify(
            @Field("userId") String userId,
            @Field("deviceId") String deviceId);


}
