package com.fame.plumbum.chataround.emergency_contacts.model;

/**
 * Created by ramya on 3/7/17.
 */

public class EmergencyContactData {
    private String name;
    private String mobile;
    // private String contactImage;
    private boolean checked;


    public EmergencyContactData(String name, String mobile, boolean checked) {
        this.name = name;
        this.mobile = mobile;
        this.checked = checked;
    }


    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }


   /* public String  getContactImage() {
        return contactImage;
    }*/

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
