package com.fame.plumbum.chataround.emergency.model.data;

import com.fame.plumbum.chataround.shouts.view.ShoutsRecyclerAdapter;

/**
 * Created by ramya on 6/7/17.
 */

public class AddEmergencyContactsData {
    private String message;
    private boolean success;
    public AddEmergencyContactsData(String message,boolean success){
        this.message=message;
        this.success=success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
