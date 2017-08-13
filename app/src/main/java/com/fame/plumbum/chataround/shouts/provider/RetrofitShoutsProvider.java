package com.fame.plumbum.chataround.shouts.provider;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;
import com.fame.plumbum.chataround.shouts.OnShoutsReceived;
import com.fame.plumbum.chataround.shouts.OnVoteResponse;
import com.fame.plumbum.chataround.shouts.api.ShoutsApi;
import com.fame.plumbum.chataround.shouts.api.ShoutsVoteApi;
import com.fame.plumbum.chataround.shouts.model.ShoutsData;
import com.fame.plumbum.chataround.shouts.model.VoteData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public class RetrofitShoutsProvider implements ShoutsProvider {


    private Retrofit retrofit;
    private Call<ShoutsData> shoutsDataCall;
    private Call<VoteData> voteDataCall;

    public RetrofitShoutsProvider() {
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
    public void requestShouts(String userId, int counter, double latitude, double longitude, final OnShoutsReceived onShoutsReceived) {

        ShoutsApi shoutsApi = retrofit.create(ShoutsApi.class);
        shoutsDataCall = shoutsApi.requestShouts(userId, counter, latitude, longitude);

        shoutsDataCall.enqueue(new Callback<ShoutsData>() {
            @Override
            public void onResponse(Call<ShoutsData> call, Response<ShoutsData> response) {
                onShoutsReceived.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ShoutsData> call, Throwable t) {
                t.printStackTrace();
                onShoutsReceived.onFailed(t.getMessage());
            }
        });
    }

    @Override
    public void requestVote(String userId, String postId, int vote_type, final OnVoteResponse onVoteResponse) {
        ShoutsVoteApi shoutsVoteApi = retrofit.create(ShoutsVoteApi.class);

        voteDataCall = shoutsVoteApi .requestVote(userId,postId,vote_type);

        voteDataCall.enqueue(new Callback<VoteData>() {
            @Override
            public void onResponse(Call<VoteData> call, Response<VoteData> response) {
                onVoteResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<VoteData> call, Throwable t) {
                t.printStackTrace();
                onVoteResponse.onFailed(t.getMessage());
            }
        });

    }


    void onDestroy() {
        if (shoutsDataCall != null) {
            shoutsDataCall.cancel();
        }

        if (voteDataCall != null) {
                voteDataCall.cancel();
        }

    }
}
