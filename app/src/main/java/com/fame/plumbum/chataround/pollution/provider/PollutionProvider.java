package com.fame.plumbum.chataround.pollution.provider;

import com.fame.plumbum.chataround.pollution.OnAirPollutionReceived;

/**
 * Created by meghal on 21/2/17.
 */

public interface PollutionProvider {

    void requestAirPollution(double latitude, double longitude, OnAirPollutionReceived onAirPollutionReceived);


}
