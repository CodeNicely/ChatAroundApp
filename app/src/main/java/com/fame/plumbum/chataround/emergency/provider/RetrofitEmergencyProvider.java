package com.fame.plumbum.chataround.emergency.provider;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;
import com.fame.plumbum.chataround.emergency.OnEmergencyContactDeleted;
import com.fame.plumbum.chataround.emergency.OnSosStartResponse;
import com.fame.plumbum.chataround.emergency.api.RequestEmergencyApiInterface;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactDeleteData;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsListData;
import com.fame.plumbum.chataround.emergency.model.StartSosData;
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
 * Created by ramya on 3/7/17.
 */

public class RetrofitEmergencyProvider implements EmergencyProvider {
    private String TAG = "EmergencyProvider";

    private RequestEmergencyApiInterface requestEmergencyApiInterface;
    private Retrofit retrofit;

    public RetrofitEmergencyProvider() {
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
    public void getEmergencyContacts(String userId, final EmergencyContactsCallback emergencyContactsCallback) {

        Call<EmergencyContactsListData> call = requestEmergencyApiInterface.getContactsList(userId);

        call.enqueue(new Callback<EmergencyContactsListData>() {
            @Override
            public void onResponse(Call<EmergencyContactsListData> call, Response<EmergencyContactsListData> response) {
                emergencyContactsCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<EmergencyContactsListData> call, Throwable t) {
                emergencyContactsCallback.onFailure("Unable to fetch contacts");

            }
        });
    }

    @Override
    public void startSos(String userId, double latitude, double longitude, final OnSosStartResponse onSosStartResponse) {

        Call<StartSosData> call = requestEmergencyApiInterface.startSos(userId, latitude, longitude);

        call.enqueue(new Callback<StartSosData>() {
            @Override
            public void onResponse(Call<StartSosData> call, Response<StartSosData> response) {
                onSosStartResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<StartSosData> call, Throwable t) {
                onSosStartResponse.onFailed(t.getMessage());
                t.printStackTrace();
            }
        });

    }

    @Override
    public void deleteContact(String userId, String mobile, final OnEmergencyContactDeleted onEmergencyContactDeleted) {
        Call<EmergencyContactDeleteData> call = requestEmergencyApiInterface.deleteContact(userId, mobile);

        call.enqueue(new Callback<EmergencyContactDeleteData>() {
            @Override
            public void onResponse(Call<EmergencyContactDeleteData> call, Response<EmergencyContactDeleteData> response) {
                onEmergencyContactDeleted.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<EmergencyContactDeleteData> call, Throwable t) {
                onEmergencyContactDeleted.onFailure(t.getMessage());
            }
        });
    }
}
