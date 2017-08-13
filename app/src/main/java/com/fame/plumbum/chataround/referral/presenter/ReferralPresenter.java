package com.fame.plumbum.chataround.referral.presenter;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface ReferralPresenter {

    void requestDeviceIdVerify(String userId,String deviceId);

    void requestReferal(String userId,String deviceId,String mobile);

    void requestReferralVerify(String userId,String deviceId,String mobile,String otp);


}
