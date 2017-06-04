package com.fame.plumbum.chataround.referal_code.model;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.referal_code.ReferalCallBack;
import com.fame.plumbum.chataround.referal_code.api.ReferalRequestApi;
import com.fame.plumbum.chataround.referal_code.model.data.ReferalData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aman on 18/5/17.
 */

public class RetrofitReferalProvider implements ReferalProvider{

    ReferalRequestApi referalRequestApi;

    public RetrofitReferalProvider() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        referalRequestApi=retrofit.create(ReferalRequestApi.class);
    }

    @Override
    public void requestReferal(String code, final ReferalCallBack referalCallBack) {
        Call<ReferalData> call = referalRequestApi.requestReferal(code);
        call.enqueue(new Callback<ReferalData>() {
            @Override
            public void onResponse(Call<ReferalData> call, Response<ReferalData> response) {
                referalCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ReferalData> call, Throwable t) {
                referalCallBack.onFailure();
            }
        });
    }
}
