package com.fame.plumbum.chataround.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by meghal on 5/3/17.
 */

public class SharedPrefs {

    private static final String PREF_NAME = "SharedPreference";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userId";
    private static String TAG = "Shared Preference";

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
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
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


}
