package com.fame.plumbum.chataround.emergency;

import com.fame.plumbum.chataround.emergency.model.EmergencyContactDeleteData;

/**
 * Created by meghalagrawal on 19/07/17.
 */

public interface OnEmergencyContactDeleted {

    void onSuccess(EmergencyContactDeleteData emergencyContactDeleteData);

    void onFailure(String message);

}
