package com.fame.plumbum.chataround.splash_screen.presenter;

import android.content.pm.PackageManager;

import com.fame.plumbum.chataround.splash_screen.SplashScreenCallback;
import com.fame.plumbum.chataround.splash_screen.model.SplashScreenHelper;
import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;
import com.fame.plumbum.chataround.splash_screen.view.SplashScreenView;

/**
 * Created by ramya on 13/3/17.
 */

public class SplashScreenPresenterImpl implements SplashScreenPresenter{
    private SplashScreenView splashScreenView;
    private SplashScreenHelper splashScreenHelper;

    public SplashScreenPresenterImpl(SplashScreenView splashScreenView, SplashScreenHelper splashScreenHelper) {
        this.splashScreenView = splashScreenView;
        this.splashScreenHelper = splashScreenHelper;
    }

    @Override
    public void getSplashResponse() {
        splashScreenHelper.getSplashData(new SplashScreenCallback() {
            @Override
            public void onSuccess(SplashScreenData splashScreenData) {
                try {
                    splashScreenView.onVersionReceived(splashScreenData);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                splashScreenView.onFailed();
            }
        });


    }
}
