package com.fame.plumbum.chataround.pollution.presenter;

/**
 * Created by meghal on 21/2/17.
 */

public interface PollutionPresenter {

    void requestAirPollution(boolean cache,double latitude, double longitude);

    void onDestroy();
}
