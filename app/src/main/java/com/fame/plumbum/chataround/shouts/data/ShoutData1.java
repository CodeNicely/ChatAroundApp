package com.fame.plumbum.chataround.shouts.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by meghal on 22/4/17.
 */

public class ShoutData1 {

    private int Status;
    private String Message;
    private List<JSONObject> Posts;

    public ShoutData1(int status, String message, List<JSONObject> posts) {
        Status = status;
        Message = message;
        Posts = posts;
    }

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public List<JSONObject> getPosts() {
        return Posts;
    }
}

