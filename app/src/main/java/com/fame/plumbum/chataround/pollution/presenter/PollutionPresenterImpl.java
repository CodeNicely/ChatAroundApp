package com.fame.plumbum.chataround.pollution.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.pollution.OnAirPollutionReceived;
import com.fame.plumbum.chataround.pollution.model.AirPollutionDetails;
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
    public void requestAirPollution(boolean cache, final double latitude, final double longitude) {

        pollutionView.showLoader(true);
        pollutionProvider.requestAirPollution(cache, latitude, longitude, new OnAirPollutionReceived() {
            @Override
            public void onSuccess(AirPollutionDetails airPollutionDetails) {
                if (pollutionView != null) {

                    if (airPollutionDetails.getStatus().equals("ok")) {

                        pollutionView.showLoader(false);
                        pollutionView.setData(airPollutionDetails);

                        Answers.getInstance().logCustom(new CustomEvent("Pollution Module Loading Successful")
                                .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)

                        );

                    } else {
                        pollutionView.showLoader(false);

                        Answers.getInstance().logCustom(new CustomEvent("Pollution Module Loading Failed")
                                .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)

                        );

                    }
                }
            }

            @Override
            public void onFailed(String message) {

                if (pollutionView != null) {
                    pollutionView.showLoader(false);
                    pollutionView.showMessage(message);
                }
            }
        });

    }

    @Override
    public void onDestroy() {

        pollutionProvider.onDestroy();

    }
}
