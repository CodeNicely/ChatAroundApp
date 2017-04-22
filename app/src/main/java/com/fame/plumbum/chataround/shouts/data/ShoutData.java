package com.fame.plumbum.chataround.shouts.data;

import java.util.List;

/**
 * Created by meghal on 22/4/17.
 */

public class ShoutData {


    private int Status;
    private String Message;
    private List<Posts> Posts;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<Posts> getPosts() {
        return Posts;
    }

    public void setPosts(List<Posts> Posts) {
        this.Posts = Posts;
    }
}
