package com.fame.plumbum.chataround.splash_screen.view;

import android.content.pm.PackageManager;

import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;

/**
 * Created by ramya on 13/3/17.
 */

public interface SplashScreenView {
    void onVersionReceived(SplashScreenData splashScreenData) throws PackageManager.NameNotFoundException;
    void onFailed();
}
