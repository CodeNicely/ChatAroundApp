package com.fame.plumbum.chataround.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by meghal on 5/3/17.
 */

public class SharedPrefs {

    private static final String PREF_NAME = "SharedPreference";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMERGENCY_MODULE ="emergencyModule";
    private static String TAG = "Shared Preference";
    private static final String KEY_FIRST_TIME_USER = "firstTimeUser";

    private static final String KEY_SHOUTS_MODULE = "shoutsModule";
    private static final String KEY_TOILET_MODULE = "toiletModule";
    private static final String KEY_GALLERY_MODULE = "galleryModule";
    private static final String KEY_POLLUTION_MODULE = "pollutionModule";
    private static final String KEY_NEWS_MODULE = "newsModule";
    private static final String KEY_USER_MOBILE_NUMBER = "userMobile";


    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;


    public SharedPrefs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    public void setUserMobile(String mobile){

        editor.putString(KEY_USER_MOBILE_NUMBER, mobile);
        // commit changes
        editor.commit();

    }

    public String getUserMobile(){
        return pref.getString(KEY_USER_MOBILE_NUMBER, null);
    }

    public boolean isShouts() {
        return pref.getBoolean(KEY_SHOUTS_MODULE, true);
    }

    public void setShouts(boolean set) {

        editor.putBoolean(KEY_SHOUTS_MODULE, set);
        editor.commit();

    }

    public boolean isToilet() {
        return pref.getBoolean(KEY_TOILET_MODULE, true);
    }

    public void setToilet(boolean set) {

        editor.putBoolean(KEY_TOILET_MODULE, set);
        editor.commit();

    }

    public boolean isGallery() {
        return pref.getBoolean(KEY_GALLERY_MODULE, true);
    }

    public void setGalleryShouts(boolean set) {

        editor.putBoolean(KEY_GALLERY_MODULE, set);
        editor.commit();

    }

    public boolean isPullution() {
        return pref.getBoolean(KEY_POLLUTION_MODULE, true);
    }

    public void setPollution(boolean set) {

        editor.putBoolean(KEY_POLLUTION_MODULE, set);
        editor.commit();

    }

    public boolean isNews() {
        return pref.getBoolean(KEY_NEWS_MODULE, true);
    }

    public void setNews(boolean set) {

        editor.putBoolean(KEY_NEWS_MODULE, set);
        editor.commit();

    }
    public void setFirstTimeUser(boolean isFirstTimeUser) {

        editor.putBoolean(KEY_FIRST_TIME_USER, isFirstTimeUser);
        editor.commit();
    }

    public boolean isFirstTimeUser() {
        return pref.getBoolean(KEY_FIRST_TIME_USER, false);
    }


    public String getUsername() {
        return pref.getString(KEY_USERNAME, "Not Available");
    }

    public void setUsername(String username) {

        editor.putString(KEY_USERNAME, username);
        editor.commit();


    }


    public void setEmailId(String emailId) {

        editor.putString(KEY_EMAIL, emailId);
        editor.commit();

    }

    public String getUserId() {

        return pref.getString(KEY_USER_ID, "Not Available");

    }

    public void setUserId(String userId) {

        editor.putString(KEY_USER_ID, userId);
        editor.commit();

    }


    public String getEmail() {

        return pref.getString(KEY_EMAIL, "Not Available");
    }


    public boolean isEmergency() {
        return pref.getBoolean(KEY_EMERGENCY_MODULE,true);
    }
    public void setEmergency(boolean set) {

        editor.putBoolean(KEY_EMERGENCY_MODULE, set);
        editor.commit();

    }
}
