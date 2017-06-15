package com.fame.plumbum.chataround.add_restroom.model.data;

/**
 * Created by meghal on 7/3/17.
 */

public class AddRestroomRequestData {

    private String username;
    private String userMobile;
    private double latitude;
    private double langitude;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private boolean male;
    private boolean female;
    private boolean disabled;
    private String mobile;


    public AddRestroomRequestData(String username, String userMobile, double latitude, double langitude, String address, String city, String state, String country, String postalCode, String knownName, boolean male, boolean female, boolean disabled, String mobile) {
        this.username = username;
        this.userMobile = userMobile;
        this.latitude = latitude;
        this.langitude = langitude;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
        this.male = male;
        this.female = female;
        this.disabled = disabled;
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public boolean isMale() {
        return male;
    }

    public boolean isFemale() {
        return female;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getMobile() {
        return mobile;
    }
}
