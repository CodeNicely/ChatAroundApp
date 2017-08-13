package com.fame.plumbum.chataround.referral.provider;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;
import com.fame.plumbum.chataround.referral.OnDeviceVerifyResponse;
import com.fame.plumbum.chataround.referral.OnReferralResponse;
import com.fame.plumbum.chataround.referral.api.ReferalRequestApi;
import com.fame.plumbum.chataround.referral.api.VerifyDeviceApi;
import com.fame.plumbum.chataround.referral.api.VerifyReferralApi;
import com.fame.plumbum.chataround.referral.model.ReferalData;
import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public class RetrofitReferralProvider implements ReferralProvider {

    private Retrofit retrofit;
    Call<VerifyDeviceData> deviceDataCall;


    public RetrofitReferralProvider() {
        OkHttpClient client;


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(RetrofitCache.provideCache()).build();

        retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
    }


    @Override
    public void requestDeviceIdVerify(String userId, String deviceId, final OnDeviceVerifyResponse onDeviceVerifyResponse) {

        VerifyDeviceApi verifyDeviceApi = retrofit.create(VerifyDeviceApi.class);
        deviceDataCall = verifyDeviceApi.requestDeviceVerify(userId, deviceId);

        deviceDataCall.enqueue(new Callback<VerifyDeviceData>() {
            @Override
            public void onResponse(Call<VerifyDeviceData> call, Response<VerifyDeviceData> response) {
                onDeviceVerifyResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<VerifyDeviceData> call, Throwable t) {
                onDeviceVerifyResponse.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });

    }

    @Override
    public void requestReferal(String userId, String deviceId, String mobile, final OnReferralResponse onReferralResponse) {

        ReferalRequestApi referalRequestApi = retrofit.create(ReferalRequestApi.class);
        Call<ReferalData> call = referalRequestApi.requestPaytmCash(userId, deviceId, mobile);
        call.enqueue(new Callback<ReferalData>() {
            @Override
            public void onResponse(Call<ReferalData> call, Response<ReferalData> response) {
                onReferralResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ReferalData> call, Throwable t) {
                t.printStackTrace();
                onReferralResponse.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void requestReferalOtp(String userId, String deviceId, String mobile, String otp, final OnReferralResponse onReferralResponse) {
        VerifyReferralApi verifyReferralApi = retrofit.create(VerifyReferralApi.class);
        Call<ReferalData> call = verifyReferralApi.requestVerifyReferral(userId, deviceId, mobile, otp);


        call.enqueue(new Callback<ReferalData>() {
            @Override
            public void onResponse(Call<ReferalData> call, Response<ReferalData> response) {

                onReferralResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ReferalData> call, Throwable t) {

                t.printStackTrace();
                onReferralResponse.onFailure(t.getMessage());
            }
        });
    }
}
