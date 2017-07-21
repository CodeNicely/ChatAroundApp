package com.fame.plumbum.chataround.emergency;

import com.fame.plumbum.chataround.emergency.model.UpdateSosData;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public interface OnLocationUpdateResponse {


    void onSuccess(UpdateSosData updateSosData);

    void onFailed(String message);

}
