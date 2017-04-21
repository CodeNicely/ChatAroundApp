package com.fame.plumbum.chataround.pollution.model;

import java.util.List;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPolutionCity {

    private List<Double> geo;
    private String name;
    private String url;


    public AirPolutionCity(List<Double> geo, String name, String url) {
        this.geo = geo;
        this.name = name;
        this.url = url;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
