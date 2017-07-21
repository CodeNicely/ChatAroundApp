package com.fame.plumbum.chataround.emergency.model;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public class UpdateSosData {

    private boolean success;
    private String message;

    public UpdateSosData(boolean success, String message) {
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
