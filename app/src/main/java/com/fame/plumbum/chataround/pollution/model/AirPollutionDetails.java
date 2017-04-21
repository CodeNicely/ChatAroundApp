package com.fame.plumbum.chataround.pollution.model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionDetails {

    private String status;
    private AirPollutionData data;

    public AirPollutionDetails(String status, AirPollutionData data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public AirPollutionData getData() {
        return data;
    }
}
