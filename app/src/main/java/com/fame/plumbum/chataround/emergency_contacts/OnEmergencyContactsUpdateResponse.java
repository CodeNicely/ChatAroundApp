package com.fame.plumbum.chataround.emergency_contacts;

import com.fame.plumbum.chataround.emergency_contacts.model.UpdateEmergencyContactsData;

/**
 * Created by ramya on 6/7/17.
 */

public interface OnEmergencyContactsUpdateResponse {
    void onSuccess(UpdateEmergencyContactsData updateEmergencyContactsData);
    void onFailure(String message);
}
