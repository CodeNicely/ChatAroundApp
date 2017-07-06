package com.fame.plumbum.chataround.emergency.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;
import com.fame.plumbum.chataround.emergency.api.EmergencyContactsApi;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeedData;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenter;
import com.fame.plumbum.chataround.emergency.presenter.EmergencyContactsPresenterImpl;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.model.NewsListData;
import com.fame.plumbum.chataround.pollution.provider.RetrofitCache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fame.plumbum.chataround.pollution.provider.RetrofitCache.REWRITE_CACHE_CONTROL_INTERCEPTOR;


/**
 * Created by ramya on 3/7/17.
 */

public class EmergencyContactsProviderImpl implements EmergencyContactsProvider {
    private String TAG = "EmergencyContactsProvider";
    private Call<EmergencyContactsFeedData> call;

    @Override
    public void getEmergencyContacts(String userId, final EmergencyContactsCallback emergencyContactsCallback) {
        OkHttpClient client;
/*
        // getting contacts ID

        //number
        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        assert phones != null;
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            EmergencyContactsFeed emergencyContactsFeed = new EmergencyContactsFeed();// adding contacts into the list
            contactsFeedList.add(emergencyContactsFeed.setData(name, phoneNumber, image_uri));
        }
        phones.close();
        if(contactsFeedList.size()!=0){
            emergencyContactsCallback.onSuccess(contactsFeedList);
        }
        else
        {
            emergencyContactsCallback.onFailure("Something went wrong");
        }*/
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .cache(RetrofitCache.provideCache()).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL).client(client).
                addConverterFactory(GsonConverterFactory.create()).build();
        EmergencyContactsApi emergencyContactsApi= retrofit.create(EmergencyContactsApi.class);
        call=emergencyContactsApi.getContactsList(userId);
        call.enqueue(new Callback<EmergencyContactsFeedData>() {
            @Override
            public void onResponse(Call<EmergencyContactsFeedData> call, Response<EmergencyContactsFeedData> response) {
                emergencyContactsCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<EmergencyContactsFeedData> call, Throwable t) {
                emergencyContactsCallback.onFailure("Unable to fetch contacts");

            }
        });
    }
}
