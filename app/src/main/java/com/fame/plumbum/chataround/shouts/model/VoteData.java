package com.fame.plumbum.chataround.shouts.model;

/**
 * Created by meghalagrawal on 09/08/17.
 */

public class VoteData {

    private boolean success;
    private String message;

    public VoteData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
