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

    public static String getReportType(int type) {
        String result = "Desconocido";
        switch (type) {
            case 1: result = "Robo con arma blanca"; break;
            case 2: result = "Robo con arma de fuego"; break;
            case 3: result = "Robo sin armas"; break;
            case 4: result = "Consumo de sustancias psicoactivas"; break;
            case 5: result = "Robo a vehículos"; break;
            case 6: result = "Grupos sospechosos"; break;
        }

        return result;
    }

    /*
        1. Robo con arma blanca,
        2. Robo con arma de fuego,
        3. Robo sin armas,
        4. Consumo de sustancias psicoactivas,
        5. Robo a vehículos
        6. Grupos sospechosos
    */

}
