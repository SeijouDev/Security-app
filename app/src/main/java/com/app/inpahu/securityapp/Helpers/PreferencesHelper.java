package com.app.inpahu.securityapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.inpahu.securityapp.Objects.User;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesHelper {

    private static final String PREFERENCES_NAME = "securityapp.prefs";
    private static final String USER_NAME = "securityapp.prefs.username";
    private static final String USER_EMAIL = "securityapp.prefs.usermail";
    private static final String USER_PASSWORD = "securityapp.prefs.userpass";
    private static final String USER_ID = "securityapp.prefs.userid";


    public static void saveUser(Context mContext, User user) {
        Editor editor = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();

        if(user.getId() != 0)
            editor.putInt(USER_ID, user.getId());

        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.apply();
    }

    public static User getUser(Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        User user = null;

        int id = prefs.getInt(USER_ID, 0);
        String name = prefs.getString(USER_NAME, null);
        String email = prefs.getString(USER_EMAIL, null);
        String password = prefs.getString(USER_PASSWORD, null);

        if (name != null)
            user = new User(id,name,email,password);

        return user;

    }

    public static void deleteUser(Context mContext) {
        Editor editor = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
