package com.fame.plumbum.chataround.referral.model;

/**
 * Created by meghalagrawal on 13/06/17.
 */

public class VerifyDeviceData {

    private boolean success;
    private String message;
    private boolean new_device;


    public VerifyDeviceData(boolean success, String message, boolean new_device) {
        this.success = success;
        this.message = message;
        this.new_device = new_device;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isNew_device() {
        return new_device;
    }
}
