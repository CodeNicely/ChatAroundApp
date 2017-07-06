package com.fame.plumbum.chataround.emergency.model;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;

/**
 * Created by ramya on 3/7/17.
 */

public interface EmergencyContactsProvider {
    void getEmergencyContacts(String userId,EmergencyContactsCallback emergencyContactsCallback);
}
