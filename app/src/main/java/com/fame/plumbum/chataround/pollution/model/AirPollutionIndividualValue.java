package com.fame.plumbum.chataround.pollution.model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionIndividualValue {

    private double v;
    private String name;
    private int color;
    private float value1;
    private float value2;
    private float value3;
    private float value4;
    private float value5;
    private float value6;

    public AirPollutionIndividualValue(double v, String name, int color, float value1, float value2, float value3, float value4, float value5, float value6) {
        this.v = v;
        this.name = name;
        this.color = color;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
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


    public float getValue1() {
        return value1;
    }

    public float getValue2() {
        return value2;
    }

    public float getValue3() {
        return value3;
    }

    public float getValue4() {
        return value4;
    }

    public float getValue5() {
        return value5;
    }

    public float getValue6() {
        return value6;
    }
}
