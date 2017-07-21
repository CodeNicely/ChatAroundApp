package com.fame.plumbum.chataround.emergency;

import com.fame.plumbum.chataround.emergency.model.EmergencyContactsListData;

/**
 * Created by ramya on 4/7/17.
 */

public interface EmergencyContactsCallback {
    void onSuccess(EmergencyContactsListData emergencyContactsListData);
    void onFailure(String message);
}
