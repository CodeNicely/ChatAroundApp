package com.fame.plumbum.chataround.referal_code.api;

import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.referal_code.model.data.ReferalData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aman on 18/5/17.
 */

public interface ReferalRequestApi {

    @GET(Urls.REFERAL_CODE)
    Call<ReferalData> requestReferal(@Query("code")String code);
}