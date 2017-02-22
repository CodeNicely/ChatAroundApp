package com.fame.plumbum.chataround.pollution.model.air_model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionIndividualValue {

    private double v;
    private String name;

    public AirPollutionIndividualValue(double v, String name) {
        this.v = v;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getV() {
        return v;
    }
}
