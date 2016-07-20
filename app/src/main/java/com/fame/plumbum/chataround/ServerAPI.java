package com.fame.plumbum.chataround;

import com.fame.plumbum.chataround.models.FromServer;
import com.google.gson.Gson;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by pankaj on 17/7/16.
 */
public interface ServerAPI {

    @Multipart
    @POST("ImageUpload")//Our Destination PHP Script
    Call<FromServer> upload(
            @Query("UserId") String userId,
            @Part MultipartBody.Part file);

    Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl("http://52.66.45.251:8080/") // REMEMBER TO END with /
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
}
