package com.fame.plumbum.chataround.emergency_contacts.api;

import com.fame.plumbum.chataround.emergency_contacts.model.UpdateEmergencyContactsData;
import com.fame.plumbum.chataround.helper.Urls;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ramya on 5/7/17.
 */

public interface UpdateEmergencyContactsApi {

    @FormUrlEncoded
    @POST(Urls.SUB_URL_UPDATE_CONTACTS)
    Call<UpdateEmergencyContactsData> updateContactsList(@Field("userId") String userId, @Field("contactList") String contactList);
}
