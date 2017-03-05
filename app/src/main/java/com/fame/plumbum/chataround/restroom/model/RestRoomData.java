package com.fame.plumbum.chataround.restroom.model;

import java.util.List;

/**
 * Created by meghal on 6/3/17.
 */

public class RestRoomData {

    private boolean success;
    private String message;
    private List<RestRoomDetails> restroom_list;


    public RestRoomData(boolean success, String message, List<RestRoomDetails> restroom_list) {
        this.success = success;
        this.message = message;
        this.restroom_list = restroom_list;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<RestRoomDetails> getRestroom_list() {
        return restroom_list;
    }
}
