package com.fame.plumbum.chataround.emergency;

import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeedData;

import java.util.List;

/**
 * Created by ramya on 4/7/17.
 */

public interface EmergencyContactsCallback {
    void onSuccess(EmergencyContactsFeedData emergencyContactsFeedData);
    void onFailure(String message);
}
