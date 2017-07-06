package com.fame.plumbum.chataround.emergency.model.data;

import android.graphics.Bitmap;

/**
 * Created by ramya on 3/7/17.
 */

public class EmergencyContactsFeed {
    private String contactName;
    private String contactNumber;
    private String contactImage;
    private boolean checked;

    public EmergencyContactsFeed(){

    }
     public EmergencyContactsFeed setData(String contactName,String contactNumber,String contactImage,boolean checked){
        EmergencyContactsFeed emergencyContactsFeed= new EmergencyContactsFeed();
        emergencyContactsFeed.contactName=contactName;
        emergencyContactsFeed.contactNumber=contactNumber;
        emergencyContactsFeed.contactImage=contactImage;
         emergencyContactsFeed.checked=checked;
        return emergencyContactsFeed;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }


    public String  getContactImage() {
        return contactImage;
    }

    public boolean isChecked() {
        return checked;
    }
}
