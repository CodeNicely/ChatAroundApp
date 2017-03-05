package com.fame.plumbum.chataround.restroom.model;

import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public class RestRoomDetails {

    private int id;
    private String name;
    private String address;
    private boolean male;
    private boolean female;
    private List<String> image_list;
    private Float latitude;
    private Float longitude;
    private String created_at;
    private String updated_at;
    private float distance;

    public RestRoomDetails(int id, String name, String address, boolean male, boolean female,
                           List<String> image_list, Float latitude, Float longitude,
                           String created_at, String updated_at, float distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.male = male;
        this.female = female;
        this.image_list = image_list;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public List<String> getImage_list() {
        return image_list;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public float getDistance() {
        return distance;
    }
}
