package com.fame.plumbum.chataround.gallery.provider;

import com.fame.plumbum.chataround.gallery.OnGalleryApiResponse;
import com.fame.plumbum.chataround.gallery.api.GalleryApi;
import com.fame.plumbum.chataround.gallery.model.GalleryData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public class RetrofitGalleryProvider implements GalleryProvider {

    private Retrofit retrofit;
    Call<GalleryData> galleryDataCall;
    @Override
    public void getImages(String userId, String mobile, double latitude, double longitude, final OnGalleryApiResponse onGalleryApiResponse) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(RetrofitCache.provideCache()).build();


        retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();

        GalleryApi galleryApi=retrofit.create(GalleryApi.class);
        galleryDataCall=galleryApi.getGalleryImages(userId,mobile,latitude,longitude);

        galleryDataCall.enqueue(new Callback<GalleryData>() {
            @Override
            public void onResponse(Call<GalleryData> call, Response<GalleryData> response) {
                onGalleryApiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<GalleryData> call, Throwable t) {
                onGalleryApiResponse.onFailed(t.getMessage());
            }
        });

    }

    @Override
    public void onDestroy() {
        if(galleryDataCall!=null){
            galleryDataCall.cancel();
        }

    }

}
