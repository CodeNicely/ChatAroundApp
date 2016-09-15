package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    int id;
    int status;
    String post_id;
    String remote_id;
    String remote_name;
    String poster_name;
    String message;
    String timestamp;

    public ChatTable(){
        status = 0;
        post_id = "";
        poster_name = "";
        remote_id = "";
        remote_name = "";
        message = "";
        timestamp = "";
    }

    public ChatTable(int status, String post_id, String remote_id, String poster_name, String remote_name, String message, String timestamp){
        this.status = status;
        this.post_id = post_id;
        this.poster_name = poster_name;
        this.remote_id = remote_id;
        this.remote_name = remote_name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public String getRemote_id() {
        return remote_id;
    }

    public String getRemote_name() {
        return remote_name;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setRemote_id(String remote_id) {
        this.remote_id = remote_id;
    }

    public void setRemote_name(String remote_name) {
        this.remote_name = remote_name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
