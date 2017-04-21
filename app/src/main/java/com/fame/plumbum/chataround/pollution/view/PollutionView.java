package com.fame.plumbum.chataround.pollution.view;

import com.fame.plumbum.chataround.pollution.model.AirPollutionDetails;

/**
 * Created by meghal on 21/2/17.
 */

public interface PollutionView {

    void showLoader(boolean show);

    void showMessage(String message);

    void setData(AirPollutionDetails airPollutionDetails);

}
