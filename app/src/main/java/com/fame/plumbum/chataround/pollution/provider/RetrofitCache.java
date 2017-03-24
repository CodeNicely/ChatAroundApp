package com.fame.plumbum.chataround.pollution.provider;

import android.util.Log;

import com.fame.plumbum.chataround.helper.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by meghal on 19/10/16.
 */

public class RetrofitCache {

    public static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {


            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(6, TimeUnit.HOURS).build();

            request = request.newBuilder().cacheControl(cacheControl).build();

            return chain.proceed(request);

        }
    };


    public static Cache provideCache() {

        Cache cache = null;
        try {
            cache = new Cache(
                    new File(
                            MyApplication
                                    .getContext()
                                    .getCacheDir()
                            , "Responses")
                    , 10 * 1024 * 1024);
        } catch (Exception e) {
            Log.e("Extra", "Could not create cache" + e.toString());
            e.printStackTrace();

        }

        return cache;

    }
}
