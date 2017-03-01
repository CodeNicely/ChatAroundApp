package com.fame.plumbum.chataround.restroom.provider;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.restroom.OnRestRoomApiResponse;
import com.fame.plumbum.chataround.restroom.api.RestroomRequestApi;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;

import java.util.List;

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

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL_RESTROOM)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    public void requestRestRooms(Double latitude, Double langitude, final OnRestRoomApiResponse onRestRoomApiResponse) {

        RestroomRequestApi restroomRequestApi = retrofit.create(RestroomRequestApi.class);
        Call<List<RestRoomData>> call = restroomRequestApi.requestRestRooms(latitude, langitude);
        call.enqueue(new Callback<List<RestRoomData>>() {
            @Override
            public void onResponse(Call<List<RestRoomData>> call, Response<List<RestRoomData>> response) {
                onRestRoomApiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<RestRoomData>> call, Throwable t) {

                t.printStackTrace();
                onRestRoomApiResponse.onFailure("Failed to connect to server");
            }
        });
    }
}
