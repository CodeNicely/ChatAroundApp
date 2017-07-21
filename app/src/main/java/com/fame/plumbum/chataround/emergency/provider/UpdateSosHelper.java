package com.fame.plumbum.chataround.emergency.provider;

import com.fame.plumbum.chataround.emergency.OnLocationUpdateResponse;
import com.fame.plumbum.chataround.emergency.OnSosStartResponse;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public interface UpdateSosHelper {

    void updateSos(String userId, String sosId, double latitude, double longitude, OnLocationUpdateResponse onLocationUpdateResponse);

}
