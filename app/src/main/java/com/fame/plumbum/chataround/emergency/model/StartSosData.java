package com.fame.plumbum.chataround.emergency.model;

/**
 * Created by meghalagrawal on 13/07/17.
 */

public class StartSosData {

    private boolean success;
    private String message;
    private String sosId;


    public StartSosData(boolean success, String message, String sosId) {
        this.success = success;
        this.message = message;
        this.sosId = sosId;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSosId() {
        return sosId;
    }
}
