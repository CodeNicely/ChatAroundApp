package com.fame.plumbum.chataround.emergency.api;

import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeedData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.model.NewsListData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ramya on 5/7/17.
 */

public interface EmergencyContactsApi {
    @GET(Urls.SUB_URL_EMERGENCY_CONTACTS)
    Call<EmergencyContactsFeedData> getContactsList(@Query("userId") String userId);

    @PUT(Urls.SUB_URL_EMERGENCY_CONTACTS)
    Call<EmergencyContactsFeedData> updateContactsList(@Path("userId") String userId,@Path("Contact name") String contactName);
}
