package com.fame.plumbum.chataround.referal_code;

import com.fame.plumbum.chataround.referal_code.model.data.ReferalData;

/**
 * Created by aman on 18/5/17.
 */

public interface ReferalCallBack {

    void onSuccess(ReferalData referalData);
    void onFailure();
}
