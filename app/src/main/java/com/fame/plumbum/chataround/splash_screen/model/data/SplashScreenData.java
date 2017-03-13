package com.fame.plumbum.chataround.splash_screen.model.data;

/**
 * Created by ramya on 13/3/17.
 */

public class SplashScreenData {
    private String message;
    private boolean success;
    private boolean compulsary_update;

    public SplashScreenData(String message, boolean success, boolean compulsary_update) {
        this.message = message;
        this.success = success;
        this.compulsary_update = compulsary_update;
    }

    public boolean isCompulsary_update() {
        return compulsary_update;
    }

    public void setCompulsary_update(boolean compulsary_update) {
        this.compulsary_update = compulsary_update;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
