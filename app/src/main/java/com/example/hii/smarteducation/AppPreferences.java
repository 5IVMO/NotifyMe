package com.example.hii.smarteducation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hii on 1/27/2016.
 */
public class AppPreferences {
    public static final String KEY_PREFS_USER_ID = "userID";
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public String getUserID() {
        return sharedPrefs.getString(KEY_PREFS_USER_ID, "");
    }

    public void saveUserID(String text) {
        prefsEditor.putString(KEY_PREFS_USER_ID, text);
        prefsEditor.commit();
    }
    public  void signOutUser(){
        prefsEditor.clear();
        prefsEditor.commit();
    }
}
