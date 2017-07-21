package com.fame.plumbum.chataround.emergency_contacts.model;

/**
 * Created by ramya on 6/7/17.
 */

public class UpdateEmergencyContactsData {
    private String message;
    private boolean success;

    public UpdateEmergencyContactsData(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
