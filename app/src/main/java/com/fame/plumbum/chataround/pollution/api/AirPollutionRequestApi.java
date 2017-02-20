package com.fame.plumbum.chataround.pollution.api;

import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghal on 21/2/17.
 */

public interface AirPollutionRequestApi {

    @GET("#")
    Call<AirPollutionDetails> requestAirPollution(
            @Query("token") String token
    );

}
