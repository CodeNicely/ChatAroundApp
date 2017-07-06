package com.fame.plumbum.chataround.emergency.model.data;

import java.util.List;

/**
 * Created by ramya on 5/7/17.
 */

public class EmergencyContactsFeedData {
    private String message;
    private boolean success;
    private List<EmergencyContactsFeed> emergencyContactsFeedList;

    public EmergencyContactsFeedData(String message, boolean success, List<EmergencyContactsFeed> emergencyContactsFeedList) {
        this.message = message;
        this.success = success;
        this.emergencyContactsFeedList = emergencyContactsFeedList;
    }
    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<EmergencyContactsFeed> getEmergencyContactsFeedList() {
        return emergencyContactsFeedList;
    }
}
