package com.fame.plumbum.chataround.emergency;

import com.fame.plumbum.chataround.emergency.model.StartSosData;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public interface OnSosStartResponse {

    void onSuccess(StartSosData startSosData);

    void onFailed(String message);

}
