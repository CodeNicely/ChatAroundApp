package com.fame.plumbum.chataround.pollution.presenter;

import com.fame.plumbum.chataround.pollution.OnAirPollutionReceived;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;
import com.fame.plumbum.chataround.pollution.provider.PollutionProvider;
import com.fame.plumbum.chataround.pollution.view.PollutionView;

/**
 * Created by meghal on 21/2/17.
 */

public class PollutionPresenterImpl implements PollutionPresenter {

    private PollutionView pollutionView;
    private PollutionProvider pollutionProvider;

    public PollutionPresenterImpl(PollutionView pollutionView, PollutionProvider pollutionProvider) {
        this.pollutionView = pollutionView;
        this.pollutionProvider = pollutionProvider;
    }

    @Override
    public void requestAirPollution(boolean cache,double latitude, double longitude) {

        pollutionView.showLoader(true);
        pollutionProvider.requestAirPollution(cache,latitude, longitude, new OnAirPollutionReceived() {
            @Override
            public void onSuccess(AirPollutionDetails airPollutionDetails) {

                if (airPollutionDetails.getStatus().equals("ok")) {
                    pollutionView.showLoader(false);
                    pollutionView.setData(airPollutionDetails);

                } else {
                    pollutionView.showLoader(false);


                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

    }
}
