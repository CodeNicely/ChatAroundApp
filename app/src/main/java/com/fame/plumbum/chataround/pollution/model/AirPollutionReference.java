package com.fame.plumbum.chataround.pollution.model;

/**
 * Created by meghal on 21/2/17.
 */

public class AirPollutionReference {

    private String url;
    private String name;

    public AirPollutionReference(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
