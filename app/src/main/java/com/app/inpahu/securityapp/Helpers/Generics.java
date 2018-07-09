package com.app.inpahu.securityapp.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.regex.Pattern;

public class Generics {

    public static boolean validateMail(String email) {
        return (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email)).find();
    }

    public static boolean validatePermissions(Context mContext, Activity activity) {
        boolean result = true;
        int locationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (locationPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED ) {
            result = false;
        }

        return result;
    }

    public static void requestPermissions(Context mContext, Activity activity) {
        String[] permissions;

        int locationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int StoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (locationPermission != PackageManager.PERMISSION_GRANTED && StoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        else if(locationPermission != PackageManager.PERMISSION_GRANTED){
            permissions = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION};
        }
        else {
            permissions = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        ActivityCompat.requestPermissions(activity, permissions , 1);
    }
}
