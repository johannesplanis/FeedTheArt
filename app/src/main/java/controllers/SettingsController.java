package controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cat.Cat;

/**
 * Created by JOHANNES on 9/25/2015.
 */
public class SettingsController {
    public static final String SETTINGS_PREFS = SharedPreferencesController.SHARED_PREFS_KEY;
    public static final String SETTINGS_STARVING_SPEED_COEFF = "SETTINGS_STARVING_SPEED_COEFF";
    public static final String SETTINGS_ALARM_LEVELS = "SETTINGS_ALARM_LEVELS";
    public static final String SETTINGS_DEFAULT_ALARM_LEVELS = "SETTINGS_DEFAULT_ALARM_LEVELS";
    public static final String SETTINGS_DEFAULT_ALARM_LEVELS_SIZE = "SETTINGS_DEFAULT_ALARM_LEVELS_SIZE";
    public static final String SETTINGS_NOTIFICATION_PERMISSION = "SETTINGS_NOTIFICATION_PERMISSION";
    public static final String DEFAULT_STARVING_SPEED = "0.0001";
    public static final float[] DEFAULT_ALARM_LEVELS = {100,25,5};

    public static final int DEFAULT_ALARM_LEVELS_SIZE = 3;
    public static boolean DEFAULT_NOTIFICATION_PERMISSION = true;
    Context context;
    private SharedPreferencesController spc;
    //private float starvingSpeed;
    private float[] alarmLevels;
    private int sizeofLevelsArray;
    private boolean notificationPermission;


    /**
     * load preferences from sp, if returned nothing, put default values
     * TODO add callback when notification status is changed
     */
    public SettingsController(Context ctxt){

        spc = new SharedPreferencesController(ctxt);

        SharedPreferences sp = null;
        if(ctxt!=null){
            this.context=ctxt;

            try {
                sp = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE);
            } catch(NullPointerException e){
                e.printStackTrace();
                SharedPreferences.Editor ed = sp.edit();
                ed.putString(SETTINGS_STARVING_SPEED_COEFF,DEFAULT_STARVING_SPEED);
                ed.commit();
            }

            this.notificationPermission = sp.getBoolean(SETTINGS_NOTIFICATION_PERMISSION,true);

            //float coeff = sp.getFloat(SETTINGS_STARVING_SPEED_COEFF, DEFAULT_STARVING_SPEED);
            //if (coeff >= 0f) {
            //    this.starvingSpeed = coeff;
            //} else {
            //    this.starvingSpeed = DEFAULT_STARVING_SPEED;

            //}
            this.sizeofLevelsArray = sp.getInt(SETTINGS_DEFAULT_ALARM_LEVELS_SIZE, DEFAULT_ALARM_LEVELS_SIZE);

            float[] helperArray = DEFAULT_ALARM_LEVELS;
            if (sizeofLevelsArray != -1) {
                for (int i = 0; i < sizeofLevelsArray; i++) {
                    helperArray[i] = sp.getFloat(SETTINGS_ALARM_LEVELS + "_" + i, DEFAULT_ALARM_LEVELS[i]);
                }
            } else {
                for (int i = 0; i < sizeofLevelsArray; i++) {
                    helperArray[i] = DEFAULT_ALARM_LEVELS[i];
                }
            }
            alarmLevels = helperArray;
        }   else{
            Log.e("SETTINGS","Context is null!");
        }


    }

    public double getStarvingSpeed() {

        SharedPreferences sp = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE);
        String stsp = sp.getString(SETTINGS_STARVING_SPEED_COEFF,"");
        Double coe = Double.parseDouble(stsp);
        return coe;
    }

    public void setStarvingSpeed(double starvingSpeed) {

        String str = String.valueOf(starvingSpeed);
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS_PREFS,Context.MODE_PRIVATE).edit();
        editor.putString(SETTINGS_STARVING_SPEED_COEFF,str);
        Log.i("SETTINGS", "Starving speed updated: "+String.valueOf(starvingSpeed));
        editor.commit();
    }


    public float[] getAlarmLevels() {
        return this.alarmLevels;
    }

    public void setAlarmLevels(float[] alarmLevels) {

        this.alarmLevels = alarmLevels;
        this.sizeofLevelsArray = alarmLevels.length;
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTINGS_PREFS,Context.MODE_PRIVATE).edit();
        for(int i=0;i<alarmLevels.length;i++){
            sp.putFloat(SETTINGS_DEFAULT_ALARM_LEVELS + "_" + i, alarmLevels[i]);
        }
        sp.putInt(SETTINGS_DEFAULT_ALARM_LEVELS_SIZE, sizeofLevelsArray);
        sp.commit();
    }

    public boolean isNotificationPermission() {
        SharedPreferences sp = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE);
        boolean np = sp.getBoolean(SETTINGS_NOTIFICATION_PERMISSION,DEFAULT_NOTIFICATION_PERMISSION);
        return np;
    }

    public void setNotificationPermission(boolean notificationPermission) {
        SharedPreferences.Editor ed = context.getSharedPreferences(SETTINGS_PREFS,Context.MODE_PRIVATE).edit();
        ed.putBoolean(SETTINGS_NOTIFICATION_PERMISSION,notificationPermission);
        ed.commit();
        this.notificationPermission = notificationPermission;
    }



    /**
     *
     * @param value value by which current speed will be changed
     * @return
     */
    public float changeStarvingSpeed(float value){
        SharedPreferences sp = context.getSharedPreferences(SETTINGS_PREFS,Context.MODE_PRIVATE);
        float coeff = sp.getFloat(SETTINGS_STARVING_SPEED_COEFF,0f);
        coeff+=value;
        if(coeff<=0){
            coeff=0;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS_PREFS,Context.MODE_PRIVATE).edit();
        editor.putFloat(SETTINGS_STARVING_SPEED_COEFF, coeff);
        editor.commit();
        return coeff;
    }

    public void refreshSettings(Cat cat){
        Log.i("REFRESHED",""+getStarvingSpeed());
        cat.setCoefficient(getStarvingSpeed());
    }



}
