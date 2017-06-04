package com.fame.plumbum.chataround.referal_code.model.data;

/**
 * Created by aman on 18/5/17.
 */

public class ReferalData {

    private String message;
    private boolean success;

    public ReferalData(String message, boolean success) {
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
