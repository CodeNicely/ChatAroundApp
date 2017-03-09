package com.fame.plumbum.chataround.image_upload.model.data;

/**
 * Created by meghal on 6/3/17.
 */

public class AddRestroomData {

    private boolean success;
    private String message;
    private String restroom_id;

    public AddRestroomData(boolean success, String message, String restroom_id) {
        this.success = success;
        this.message = message;
        this.restroom_id = restroom_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getRestroom_id() {
        return restroom_id;
    }
}
