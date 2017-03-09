package com.fame.plumbum.chataround.restroom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public class RestRoomDetails {

    private String username;
    private String address;
    private boolean male;
    private boolean female;
    private boolean disabled;
    private ArrayList<String> image_list;
    private float latitude;
    private float longitude;
    private String created_at;
    private String updated_at;
    private int distance;

    public RestRoomDetails(String username, String address, boolean male, boolean female, boolean disabled, ArrayList<String> image_list, float latitude, float longitude, String created_at, String updated_at, int distance) {
        this.username = username;
        this.address = address;
        this.male = male;
        this.female = female;
        this.disabled = disabled;
        this.image_list = image_list;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.distance = distance;

    }


    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
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

    public ArrayList<String> getImage_list() {
        return image_list;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getDistance() {
        return distance;
    }
}


