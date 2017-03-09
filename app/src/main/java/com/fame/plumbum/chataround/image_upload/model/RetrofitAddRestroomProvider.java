package com.fame.plumbum.chataround.image_upload.model;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.image_upload.OnAddRestroomResponse;
import com.fame.plumbum.chataround.image_upload.api.AddRestroomApi;
import com.fame.plumbum.chataround.image_upload.model.data.AddRestroomData;
import com.fame.plumbum.chataround.image_upload.model.data.AddRestroomRequestData;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghal on 6/3/17.
 */

public class RetrofitAddRestroomProvider implements AddRestroomProvider {

    Retrofit retrofit;

    public RetrofitAddRestroomProvider() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void addRestroom(AddRestroomRequestData addRestroomRequestData, final OnAddRestroomResponse onAddRestroomResponse) {

        AddRestroomApi addRestroomApi = retrofit.create(AddRestroomApi.class);
        Call<AddRestroomData> call = addRestroomApi.addRestroom(
                addRestroomRequestData.getUsername(),
                addRestroomRequestData.getLatitude(),
                addRestroomRequestData.getLangitude(),
                addRestroomRequestData.getAddress(),
                addRestroomRequestData.getCity(),
                addRestroomRequestData.getState(),
                addRestroomRequestData.getCountry(),
                addRestroomRequestData.getPostalCode(),
                addRestroomRequestData.getKnownName(),
                addRestroomRequestData.isMale(),
                addRestroomRequestData.isFemale(),
                addRestroomRequestData.isDisabled()
        );

        call.enqueue(new Callback<AddRestroomData>() {
            @Override
            public void onResponse(Call<AddRestroomData> call, Response<AddRestroomData> response) {
                onAddRestroomResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AddRestroomData> call, Throwable t) {
                onAddRestroomResponse.onFailure("Unable to Call to Network , " +
                        "Please Check Your Internet Connection");
                t.printStackTrace();
            }
        });

    }


}
