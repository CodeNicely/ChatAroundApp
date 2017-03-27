package com.fame.plumbum.chataround.restroom.provider;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;
import com.fame.plumbum.chataround.restroom.OnRestRoomApiResponse;
import com.fame.plumbum.chataround.restroom.api.RestroomRequestApi;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghal on 19/2/17.
 */

public class RetrofitRestRoomProvider implements RestRoomProvider {

    private Retrofit retrofit;

    public RetrofitRestRoomProvider() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(RetrofitCache.provideCache())
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    public void requestRestRooms(String user_id, double latitude, double langitude, final OnRestRoomApiResponse onRestRoomApiResponse) {

        RestroomRequestApi restroomRequestApi = retrofit.create(RestroomRequestApi.class);
        Call<RestRoomData> call = restroomRequestApi.requestRestRooms(user_id, latitude, langitude);
        /*call.enqueue(new Callback<RestRoomData>() {
            @Override
            public void onResponse(Call<List<RestRoomDetails>> call, Response<List<RestRoomDetails>> response) {
                onRestRoomApiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<RestRoomDetails>> call, Throwable t) {

                t.printStackTrace();
                onRestRoomApiResponse.onFailure("Failed to connect to server");
            }
        });*/

        call.enqueue(new Callback<RestRoomData>() {
            @Override
            public void onResponse(Call<RestRoomData> call, Response<RestRoomData> response) {
                onRestRoomApiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<RestRoomData> call, Throwable t) {

                onRestRoomApiResponse.onFailure("Something Went Wrong");
            }
        });
    }
}
