package com.fame.plumbum.chataround.queries;

import com.fame.plumbum.chataround.models.ImageSendData;
import com.fame.plumbum.chataround.models.PostData;
import com.google.gson.Gson;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by pankaj on 17/7/16.
 */
public interface ServerAPI {

    @Multipart
    @POST("ImageUpload")
    Call<ImageSendData> upload(
            @Query("UserId") String userId,
            @Part MultipartBody.Part file);

    @GET("POST")
            Call<PostData> postData(
            @Query("UserId") String userId,
            @Query("UserName") String userName,
            @Query("Post") String post,
            @Query("Latitude") String lat,
            @Query("Longitude") String lon
    );

    Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl("http://52.66.45.251:8080/") // REMEMBER TO END with /
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
}
