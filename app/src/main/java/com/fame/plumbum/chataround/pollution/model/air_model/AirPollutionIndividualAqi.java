package com.fame.plumbum.chataround.pollution.model.air_model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionIndividualAqi {


    private AirPollutionIndividualValue pm10;
    private AirPollutionIndividualValue pm25;
    private AirPollutionIndividualValue no2;
    private AirPollutionIndividualValue o3;
    private AirPollutionIndividualValue co;
    private AirPollutionIndividualValue s02;


    private AirPollutionIndividualValue d;

    private AirPollutionIndividualValue t;
    private AirPollutionIndividualValue p;

    private AirPollutionIndividualValue h;
    private AirPollutionIndividualValue w;

    private AirPollutionIndividualValue wd;


    public AirPollutionIndividualAqi(AirPollutionIndividualValue co, AirPollutionIndividualValue d, AirPollutionIndividualValue h, AirPollutionIndividualValue no2, AirPollutionIndividualValue o3, AirPollutionIndividualValue p, AirPollutionIndividualValue pm10, AirPollutionIndividualValue pm25, AirPollutionIndividualValue s02, AirPollutionIndividualValue t, AirPollutionIndividualValue w, AirPollutionIndividualValue wd) {
        this.co = co;
        this.d = d;
        this.h = h;
        this.no2 = no2;
        this.o3 = o3;
        this.p = p;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.s02 = s02;
        this.t = t;
        this.w = w;
        this.wd = wd;
    }

    public AirPollutionIndividualValue getCo() {
        return co;
    }

    public AirPollutionIndividualValue getD() {
        return d;
    }

    public AirPollutionIndividualValue getH() {
        return h;
    }

    public AirPollutionIndividualValue getNo2() {
        return no2;
    }

    public AirPollutionIndividualValue getO3() {
        return o3;
    }

    public AirPollutionIndividualValue getP() {
        return p;
    }

    public AirPollutionIndividualValue getPm10() {
        return pm10;
    }

    public AirPollutionIndividualValue getPm25() {
        return pm25;
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

    public AirPollutionIndividualValue getWd() {
        return wd;
    }
}
