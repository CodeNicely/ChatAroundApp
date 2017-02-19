package com.fame.plumbum.chataround.restroom.model;

/**
 * Created by meghal on 19/2/17.
 */

public class RestRoomData {

    private int id;
    private String name;
    private String city;
    private String state;
    private boolean accessible;
    private boolean unisex;
    private String directions;
    private String comment;
    private Float latitude;
    private Float longitude;
    private String created_at;
    private String updated_at;
    private int downvote;
    private int upvote;
    private String country;
    private float distance;
    private String bearing;

    public RestRoomData(int id, String name, String city, String state, boolean accessible, boolean unisex, String directions, String comment, Float latitude, Float longitude, String created_at, String updated_at, int downvote, int upvote, String country, float distance, String bearing) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.state = state;
        this.accessible = accessible;
        this.unisex = unisex;
        this.directions = directions;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.downvote = downvote;
        this.upvote = upvote;
        this.country = country;
        this.distance = distance;
        this.bearing = bearing;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public boolean isUnisex() {
        return unisex;
    }

    public String getDirections() {
        return directions;
    }

    public String getComment() {
        return comment;
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

    public int getDownvote() {
        return downvote;
    }

    public int getUpvote() {
        return upvote;
    }

    public String getCountry() {
        return country;
    }

    public float getDistance() {
        return distance;
    }

    public String getBearing() {
        return bearing;
    }
}
