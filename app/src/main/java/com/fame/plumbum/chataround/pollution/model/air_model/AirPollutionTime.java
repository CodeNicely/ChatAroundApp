package com.fame.plumbum.chataround.pollution.model.air_model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionTime {

    private String s;

    private String tz;

    private int v;

    public AirPollutionTime(String s, String tz, int v) {
        this.s = s;
        this.tz = tz;
        this.v = v;
    }


    public String getS() {
        return s;
    }

    public String getTz() {
        return tz;
    }

    public int getV() {
        return v;
    }
}
