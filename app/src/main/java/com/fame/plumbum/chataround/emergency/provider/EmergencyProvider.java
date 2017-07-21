package com.fame.plumbum.chataround.emergency.provider;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;
import com.fame.plumbum.chataround.emergency.OnEmergencyContactDeleted;
import com.fame.plumbum.chataround.emergency.OnSosStartResponse;

/**
 * Created by ramya on 3/7/17.
 */

public interface EmergencyProvider {
    void getEmergencyContacts(String userId, EmergencyContactsCallback emergencyContactsCallback);

    void startSos(String userId, double latitude, double longitude, OnSosStartResponse onSosStartResponse);

    void deleteContact(String userId, String mobile, OnEmergencyContactDeleted onEmergencyContactDeleted);

}
