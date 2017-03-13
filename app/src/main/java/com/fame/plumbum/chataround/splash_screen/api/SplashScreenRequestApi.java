package com.fame.plumbum.chataround.splash_screen.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ramya on 13/3/17.
 */

public interface SplashScreenRequestApi {
    @GET(Urls.SUB_URL_SPLASH_SCREEN)
    Call<SplashScreenData> getSplashScreenResponse();
}
