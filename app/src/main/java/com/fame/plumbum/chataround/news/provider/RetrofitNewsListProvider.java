package com.fame.plumbum.chataround.news.provider;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;
import com.fame.plumbum.chataround.news.api.NewsListRequestApi;
import com.fame.plumbum.chataround.news.model.NewsListData;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fame.plumbum.chataround.pollution.provider.RetrofitCache.REWRITE_CACHE_CONTROL_INTERCEPTOR;

/**
 * Created by ramya on 10/3/17.
 */

public class RetrofitNewsListProvider implements NewsListProvider {
    private Call<NewsListData> call;

    @Override
    public void getNewsList(boolean cache, String userId, String city, String state, String country, final NewsFeedRequestCallback newsFeedRequestCallback) {

        OkHttpClient client;

        if (cache) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .cache(RetrofitCache.provideCache()).build();
        } else {


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .cache(RetrofitCache.provideCache()).build();

        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
        NewsListRequestApi newsListRequestApi = retrofit.create(NewsListRequestApi.class);
        call = newsListRequestApi.getNewsList(userId, city, state, country);
        call.enqueue(new Callback<NewsListData>() {
            @Override
            public void onResponse(Call<NewsListData> call, Response<NewsListData> response) {
                newsFeedRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NewsListData> call, Throwable t) {
                newsFeedRequestCallback.OnFailure("Unable to fetch News for your Location!");
            }
        });
    }

    @Override
    public void onDestroy() {
        if (call != null)
            call.cancel();
    }
}
