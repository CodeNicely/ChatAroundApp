package com.fame.plumbum.chataround.splash_screen.model;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.splash_screen.SplashScreenCallback;
import com.fame.plumbum.chataround.splash_screen.api.SplashScreenRequestApi;
import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ramya on 13/3/17.
 */

public class RetrofitSplashScreenProvider implements SplashScreenHelper {
    private SplashScreenRequestApi splashScreenRequestApi;
    public RetrofitSplashScreenProvider() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        splashScreenRequestApi = retrofit.create(SplashScreenRequestApi.class);
    }
    @Override
    public void getSplashData(final SplashScreenCallback splashScreenCallback) {
        Call<SplashScreenData> splashScreenDataCall=splashScreenRequestApi.getSplashScreenResponse();
        splashScreenDataCall.enqueue(new Callback<SplashScreenData>() {
            @Override
            public void onResponse(Call<SplashScreenData> call, Response<SplashScreenData> response) {
                splashScreenCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<SplashScreenData> call, Throwable t) {
                splashScreenCallback.onFailure();
            }
        });


    }
}
