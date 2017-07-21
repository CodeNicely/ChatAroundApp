package com.fame.plumbum.chataround.emergency.model;

import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;

import java.util.List;

/**
 * Created by ramya on 5/7/17.
 */

public class EmergencyContactsListData {
    private String message;
    private boolean success;
    private List<EmergencyContactData> contactList;

    public EmergencyContactsListData(String message, boolean success, List<EmergencyContactData> contactList) {
        this.message = message;
        this.success = success;
        this.contactList = contactList;
    }
    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<EmergencyContactData> getContactList() {
        return contactList;
    }
}
