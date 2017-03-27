package com.fame.plumbum.chataround.pollution.provider;

import com.fame.plumbum.chataround.pollution.OnAirPollutionReceived;

/**
 * Created by meghal on 21/2/17.
 */

public interface PollutionProvider {

    void requestAirPollution(boolean cache,double latitude, double longitude, OnAirPollutionReceived onAirPollutionReceived);


}
