package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class PostTable {
    int id;
    String post_id;
    String poster_id;
    String poster_name;
    String message;
    int no_likes;
    String comment_ids;
    String lat;
    String lon;
    String timestamp;

    public PostTable(){
        post_id = "";
        poster_id = "";
        poster_name = "";
        message = "";
        no_likes = 0;
        comment_ids = "";
        lat = "";
        lon = "";
        timestamp = "";
    }

    public  PostTable(String post_id, String poster_id, String poster_name, String message, int no_likes, String comment_ids, String lat, String lon, String timestamp){
        this.timestamp = timestamp;
        this.lon = lon;
        this.lat = lat;
        this.poster_name = poster_name;
        this.comment_ids = comment_ids;
        this.message = message;
        this.poster_id = poster_id;
        this.post_id = post_id;
        this.no_likes = no_likes;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setComment_ids(String comment_ids) {
        this.comment_ids = comment_ids;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setNo_likes(int no_likes) {
        this.no_likes = no_likes;
    }

    public void setPoster_id(String poster_id) {
        this.poster_id = poster_id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public int getId() {
        return id;
    }

    public int getNo_likes() {
        return no_likes;
    }

    public String getComment_ids() {
        return comment_ids;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getPoster_id() {
        return poster_id;
    }
}
