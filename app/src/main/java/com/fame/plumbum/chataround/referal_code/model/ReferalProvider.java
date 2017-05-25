package com.fame.plumbum.chataround.referal_code.model;

import com.fame.plumbum.chataround.referal_code.ReferalCallBack;

/**
 * Created by aman on 18/5/17.
 */

public interface ReferalProvider {
    void requestReferal(String code, ReferalCallBack referalCallBack);

}
