package com.fame.plumbum.chataround.referral.provider;

import com.fame.plumbum.chataround.referral.OnDeviceVerifyResponse;
import com.fame.plumbum.chataround.referral.OnReferralResponse;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface ReferralProvider {

    void requestDeviceIdVerify(String userId, String deviceId, OnDeviceVerifyResponse onDeviceVerifyResponse);

    void requestReferal(String userId,String deviceId,String mobile, OnReferralResponse onReferralResponse);


}
