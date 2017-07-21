package com.fame.plumbum.chataround.emergency_contacts.provider;

import com.fame.plumbum.chataround.emergency_contacts.OnEmergencyContactsUpdateResponse;
import com.fame.plumbum.chataround.emergency_contacts.api.UpdateEmergencyContactsApi;
import com.fame.plumbum.chataround.emergency_contacts.model.UpdateEmergencyContactsData;
import com.fame.plumbum.chataround.helper.Urls;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meghalagrawal on 12/07/17.
 */

public class RetrofitEmergencyContactsUpdateProvider implements EmergencyContactsUpdateProvider {

    private Retrofit retrofit;

    public RetrofitEmergencyContactsUpdateProvider() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();


        retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Override
    public void updateContactsList(String userId, String contactList, final OnEmergencyContactsUpdateResponse onEmergencyContactsUpdateResponse) {

        UpdateEmergencyContactsApi updateEmergencyContactsApi = retrofit.create(UpdateEmergencyContactsApi.class);

        Call<UpdateEmergencyContactsData> call = updateEmergencyContactsApi.updateContactsList(userId, contactList);

        call.enqueue(new Callback<UpdateEmergencyContactsData>() {
            @Override
            public void onResponse(Call<UpdateEmergencyContactsData> call, Response<UpdateEmergencyContactsData> response) {
                onEmergencyContactsUpdateResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UpdateEmergencyContactsData> call, Throwable t) {

                t.printStackTrace();
                onEmergencyContactsUpdateResponse.onFailure(t.getMessage());
            }
        });


    }
}
