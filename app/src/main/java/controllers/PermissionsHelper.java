package controllers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by JOHANNES on 4/5/2016.
 */
public class PermissionsHelper {

    public static boolean checkLocationPermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            return true;
        }
    }
