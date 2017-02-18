package com.fame.plumbum.chataround.queries;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.models.ImageSendData;
import com.fame.plumbum.chataround.models.PostData;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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
    // Query 1:
    @Multipart
    @POST("ImageUpload")
    // ImageUpload is the function name in PHP.
    Call<ImageSendData> upload(
            @Query("UserId") String userId,
            @Part MultipartBody.Part file);
    // Write arguments of PHP function above.

    // This is another query:
    @GET("POST")
    Call<PostData> postData(
            @Query("UserId") String userId,
            @Query("UserName") String userName,
            @Query("Post") String post,
            @Query("Latitude") String lat,
            @Query("Longitude") String lon
    );

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS);

    // This is the main variable. In base url, write location of your server, without the function in php file as that is defined above.
// And in client, just create a new client instead of using the above one. That is just for loggnig purposes.            
    Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL) // REMEMBER TO END with /
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(httpClient.build())
                    .build();
}