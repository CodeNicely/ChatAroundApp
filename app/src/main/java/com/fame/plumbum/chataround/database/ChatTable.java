package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    int id;
    int post_id;
    String remote_id;
    int status;
    String comment;
    String timestamp;

    public ChatTable(){
        remote_id = "";
        status = 0;
        comment = "";
        timestamp = "";
    }

    public ChatTable(int status, String remote_id, String comment, String timestamp){
        this.timestamp = timestamp;
        this.comment = comment;
        this.status = status;
        this.remote_id = remote_id;
    }

    public int getId() {
        return id;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getRemote_id() {
        return remote_id;
    }

    public int getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setRemote_id(String remote_id) {
        this.remote_id = remote_id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
