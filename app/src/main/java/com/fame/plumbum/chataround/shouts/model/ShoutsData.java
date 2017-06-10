package com.fame.plumbum.chataround.shouts.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by meghalagrawal on 10/06/17.
 */

public class ShoutsData {


    @SerializedName("Status")
    private int Status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Posts")
    private List<Posts> Posts;

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public List<Posts> getPosts() {
        return Posts;
    }
}
