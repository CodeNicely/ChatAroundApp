package com.fame.plumbum.chataround.pollution;

import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;

/**
 * Created by meghal on 20/2/17.
 */

public interface OnAirPollutionReceived {

    void onSuccess(AirPollutionDetails airPollutionDetails);
    void onFailed(String message);


}
