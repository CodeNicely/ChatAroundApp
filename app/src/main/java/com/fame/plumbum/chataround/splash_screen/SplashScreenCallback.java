package com.fame.plumbum.chataround.splash_screen;

import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;

/**
 * Created by ramya on 13/3/17.
 */

public interface SplashScreenCallback {
    void onSuccess(SplashScreenData splashScreenData);
    void onFailure();
}
