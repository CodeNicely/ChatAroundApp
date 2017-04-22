package com.fame.plumbum.chataround.helper;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by meghal on 11/10/16.
 */

public class MyApplication extends MultiDexApplication {

    private static Context context;
    private SharedPrefs sharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // TODO: Move this to where you establish a user session


    }



    public static Context getContext() {
        return context;
    }


}
