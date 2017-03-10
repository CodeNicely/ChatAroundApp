package com.fame.plumbum.chataround.news.model;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.NewsFeedRequestCallback;
import com.fame.plumbum.chataround.news.api.NewsListRequestApi;
import com.fame.plumbum.chataround.news.model.data.NewsListData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ramya on 10/3/17.
 */

public class RetrofitNewsListProvider implements NewsListProvider {
    private static final String TAG="NewsListProvider";
    private NewsListRequestApi newsListRequestApi;


    @Override
    public void getNewsList(String  userId, String city, final NewsFeedRequestCallback newsFeedRequestCallback) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
        NewsListRequestApi newsListRequestApi = retrofit.create(NewsListRequestApi.class);
        Call<NewsListData> call=newsListRequestApi.getNewsList(userId,city);
        call.enqueue(new Callback<NewsListData>() {
            @Override
            public void onResponse(Call<NewsListData> call, Response<NewsListData> response) {
                newsFeedRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NewsListData> call, Throwable t) {
                newsFeedRequestCallback.OnFailure(t.getMessage());
            }
        });
    }
}
