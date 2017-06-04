package com.fame.plumbum.chataround.referal_code.model;

import com.fame.plumbum.chataround.referal_code.ReferalCallBack;
import com.fame.plumbum.chataround.referal_code.model.data.ReferalData;
import com.fame.plumbum.chataround.referal_code.presenter.ReferalPresenter;

/**
 * Created by aman on 19/5/17.
 */

public class MockReferalProvider implements ReferalProvider{


    @Override
    public void requestReferal(String code, final ReferalCallBack referalCallBack) {

        ReferalData referalData = new ReferalData("Success",true);
        referalCallBack.onSuccess(referalData);
    }
}
