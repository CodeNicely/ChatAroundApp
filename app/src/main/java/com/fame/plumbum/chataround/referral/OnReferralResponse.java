package com.fame.plumbum.chataround.referral;

import com.fame.plumbum.chataround.referral.model.ReferalData;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public interface OnReferralResponse {

    void onSuccess(ReferalData referralData);

    void onFailure(String message);

}
