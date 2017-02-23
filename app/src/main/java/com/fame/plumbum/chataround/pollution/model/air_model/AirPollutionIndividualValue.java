package com.fame.plumbum.chataround.pollution.model.air_model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionIndividualValue {

    private double v;
    private String name;
    private int color;

    public AirPollutionIndividualValue(double v, String name, int color) {
        this.v = v;
        this.name = name;
        this.color = color;
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

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
