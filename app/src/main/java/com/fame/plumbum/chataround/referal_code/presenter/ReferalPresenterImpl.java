package com.fame.plumbum.chataround.referal_code.presenter;

import android.widget.Toast;

import com.fame.plumbum.chataround.activity.LoginActivity;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.referal_code.ReferalCallBack;
import com.fame.plumbum.chataround.referal_code.model.ReferalProvider;
import com.fame.plumbum.chataround.referal_code.model.data.ReferalData;

/**
 * Created by aman on 18/5/17.
 */

public class ReferalPresenterImpl implements ReferalPresenter{

    private ReferalProvider referalProvider;
    private LoginActivity loginActivity;
    private SharedPrefs sharedPrefs;

    public ReferalPresenterImpl(ReferalProvider referalProvider, LoginActivity loginActivity) {
        this.referalProvider = referalProvider;
        this.loginActivity = loginActivity;
        this.sharedPrefs = new SharedPrefs(loginActivity);
    }

    @Override
    public void requestReferal(String code) {
        referalProvider.requestReferal(code, new ReferalCallBack() {
            @Override
            public void onSuccess(ReferalData referalData) {
                if(referalData.isSuccess())
                {
                    sharedPrefs.setFirstTimeUser(true);
                    Toast.makeText(loginActivity,referalData.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(loginActivity,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure() {
                Toast.makeText(loginActivity,"Low Speed Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
