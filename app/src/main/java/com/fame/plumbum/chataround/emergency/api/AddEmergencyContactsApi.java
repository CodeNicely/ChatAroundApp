package com.fame.plumbum.chataround.emergency.api;

import com.fame.plumbum.chataround.emergency.model.data.AddEmergencyContactsData;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeedData;
import com.fame.plumbum.chataround.helper.Urls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by ramya on 6/7/17.
 */

public interface AddEmergencyContactsApi {
    @POST(Urls.SUB_URL_ADD_CONTACTS)
    Call<AddEmergencyContactsData> addContacts(@Field("userId") String userId, @Field("contactsList")List<EmergencyContactsFeed> emergencyContactsFeedList);
}
