package com.fame.plumbum.chataround.referral.view;

import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface ReferralView {

    void showDialogLoader(boolean show);

    void onDeviceDataReceived(VerifyDeviceData verifyDeviceData);

    void showMessage(String message);

    void onReferralRequestSent();

    void onReferralSuccess();
}
