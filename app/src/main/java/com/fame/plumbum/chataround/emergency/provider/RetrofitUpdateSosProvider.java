package com.fame.plumbum.chataround.emergency.provider;

import com.fame.plumbum.chataround.emergency.OnLocationUpdateResponse;
import com.fame.plumbum.chataround.emergency.api.RequestEmergencyApiInterface;
import com.fame.plumbum.chataround.emergency.model.UpdateSosData;
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
 * Created by meghalagrawal on 13/07/17.
 */

public class RetrofitUpdateSosProvider implements UpdateSosHelper {


    private Retrofit retrofit;
    private RequestEmergencyApiInterface requestEmergencyApiInterface;

    public RetrofitUpdateSosProvider() {
        OkHttpClient client;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(RetrofitCache.provideCache()).build();

        retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
        requestEmergencyApiInterface = retrofit.create(RequestEmergencyApiInterface.class);

    }

    @Override
    public void updateSos(String userId, String sosId, double latitude, double longitude, final OnLocationUpdateResponse onLocationUpdateResponse) {

        Call<UpdateSosData> call = requestEmergencyApiInterface.updateSosData(userId, sosId, latitude, longitude);

        call.enqueue(new Callback<UpdateSosData>() {
            @Override
            public void onResponse(Call<UpdateSosData> call, Response<UpdateSosData> response) {
                onLocationUpdateResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UpdateSosData> call, Throwable t) {


                t.printStackTrace();
                onLocationUpdateResponse.onFailed(t.getMessage());
            }
        });


    }
}