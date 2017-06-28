package com.fame.plumbum.chataround.referral.presenter;

import android.util.Log;
import android.widget.Toast;

import com.fame.plumbum.chataround.referral.OnDeviceVerifyResponse;
import com.fame.plumbum.chataround.referral.OnReferralResponse;
import com.fame.plumbum.chataround.referral.model.ReferalData;
import com.fame.plumbum.chataround.referral.model.VerifyDeviceData;
import com.fame.plumbum.chataround.referral.provider.ReferralProvider;
import com.fame.plumbum.chataround.referral.view.ReferralView;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public class ReferralPresenterImpl implements ReferralPresenter {

    private static final String TAG = "ReferralPresenterTag";
    private ReferralView referralView;
    private ReferralProvider referralProvider;

    public ReferralPresenterImpl(ReferralView referralView, ReferralProvider referralProvider) {
        this.referralView = referralView;
        this.referralProvider = referralProvider;
    }

    @Override
    public void requestDeviceIdVerify(String userId, String deviceId) {
        referralProvider.requestDeviceIdVerify(userId, deviceId, new OnDeviceVerifyResponse() {
            @Override
            public void onSuccess(VerifyDeviceData verifyDeviceData) {
                if (verifyDeviceData != null) {
                    if (verifyDeviceData.isSuccess()) {
                        referralView.onDeviceDataReceived(verifyDeviceData);
                    } else {

                        Log.d(TAG, "Device is an old device, So we will not show a referral dialog");

                    }
                }
            }
            @Override
            public void onFailure(String message) {
                referralView.showMessage(message);
                Log.e(TAG, "Device data API call unsuccessful");

            }
        });
    }


    @Override
    public void requestReferal(String userId, String deviceId, String mobile) {
        referralView.showDialogLoader(true);
        referralProvider.requestReferal(userId, deviceId, mobile, new OnReferralResponse() {
            @Override
            public void onSuccess(ReferalData body) {
                referralView.showDialogLoader(false);
            }

            @Override
            public void onFailure(String message) {
                referralView.showMessage(message);
                referralView.showDialogLoader(false);

            }
        });
    }
}
