package com.fame.plumbum.chataround.referral;

import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface OnDeviceVerifyResponse {

    void onSuccess(VerifyDeviceData verifyDeviceData);
    void onFailure(String message);


}
