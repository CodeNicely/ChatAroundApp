package com.fame.plumbum.chataround.emergency.model;

/**
 * Created by meghalagrawal on 17/07/17.
 */

public class EmergencyContactDeleteData {

    private boolean success;
    private String message;


    public EmergencyContactDeleteData(boolean success, String message) {
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
