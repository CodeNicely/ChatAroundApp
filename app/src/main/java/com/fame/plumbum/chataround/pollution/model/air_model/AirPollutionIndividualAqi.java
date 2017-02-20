package com.fame.plumbum.chataround.pollution.model.air_model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionIndividualAqi {


    private AirPollutionIndividualValue co;
    private AirPollutionIndividualValue h;
    private AirPollutionIndividualValue p;
    private AirPollutionIndividualValue pm10;
    private AirPollutionIndividualValue s02;
    private AirPollutionIndividualValue t;
    private AirPollutionIndividualValue w;

    public AirPollutionIndividualAqi(AirPollutionIndividualValue co, AirPollutionIndividualValue h, AirPollutionIndividualValue p, AirPollutionIndividualValue pm10, AirPollutionIndividualValue s02, AirPollutionIndividualValue t, AirPollutionIndividualValue w) {
        this.co = co;
        this.h = h;
        this.p = p;
        this.pm10 = pm10;
        this.s02 = s02;
        this.t = t;
        this.w = w;
    }

    public AirPollutionIndividualValue getCo() {
        return co;
    }

    public AirPollutionIndividualValue getH() {
        return h;
    }

    public AirPollutionIndividualValue getP() {
        return p;
    }

    public AirPollutionIndividualValue getPm10() {
        return pm10;
    }

    public AirPollutionIndividualValue getS02() {
        return s02;
    }

    public AirPollutionIndividualValue getT() {
        return t;
    }

    public AirPollutionIndividualValue getW() {
        return w;
    }
}
