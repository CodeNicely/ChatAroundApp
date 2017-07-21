package com.fame.plumbum.chataround.emergency_contacts.provider;

import com.fame.plumbum.chataround.emergency_contacts.OnEmergencyContactsUpdateResponse;

/**
 * Created by meghalagrawal on 12/07/17.
 */

public interface EmergencyContactsUpdateProvider {

    void updateContactsList(String userId, String contactList, OnEmergencyContactsUpdateResponse onEmergencyContactsUpdateResponse);

}
