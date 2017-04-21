package com.fame.plumbum.chataround.pollution.model;

import java.util.List;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionData {

    private int aqi;
    private int idx;
    private List<AirPollutionReference> attributions;
    private AirPolutionCity city;
    private String dominentpol;
    private AirPollutionTime time;

    private AirPollutionIndividualAqi iaqi;

    public AirPollutionData(int aqi, int idx, List<AirPollutionReference> attributions, AirPolutionCity city, String dominentpol, AirPollutionTime time, AirPollutionIndividualAqi iaqi) {
        this.aqi = aqi;
        this.idx = idx;
        this.attributions = attributions;
        this.city = city;
        this.dominentpol = dominentpol;
        this.time = time;
        this.iaqi = iaqi;
    }

    public int getAqi() {
        return aqi;
    }

    public int getIdx() {
        return idx;
    }

    public List<AirPollutionReference> getAttributions() {
        return attributions;
    }

    public AirPolutionCity getCity() {
        return city;
    }

    public String getDominentpol() {
        return dominentpol;
    }

    public AirPollutionTime getTime() {
        return time;
    }

    public AirPollutionIndividualAqi getIaqi() {
        return iaqi;
    }
}
