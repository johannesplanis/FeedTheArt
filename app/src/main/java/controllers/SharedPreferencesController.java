package controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import cat.Cat;

/**
 * put int SharedPreferences objects, Strings, numbers in formats: float, int, double
 * get from SharedPreferences objects, Strings, numbers in formats: float, int, double
 * Created by JOHANNES on 9/27/2015.
 */
public class SharedPreferencesController {
    public static final String SHARED_PREFS_KEY = "SHARED PREFERENCES";
    private final SharedPreferences sp;
    private SharedPreferences.Editor ed;
    public SharedPreferencesController(Context context){
        this.sp = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);

    }

    /**
     * put Cat object into Shared preferences with given key
     *
     * @param key
     * @param c
     */
    public void putCat(String key,Cat c){
        Log.i("CREATOR PUT","1");
        if(!sp.getString(key,"").equals("")){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w. message: "+sp.getString(key,""));
        }
        Log.i("CREATOR PUT","2");
        ed = sp.edit();
        Log.i("CREATOR PUT","3");
        Gson gson = new Gson();
        Log.i("CREATOR PUT","4");
        String serialized = gson.toJson(c);  //jebana linijka kodu, zasrany kot nie chce się serializować
        Log.i("CREATOR PUT","5");
        ed.putString(key, serialized);
        Log.i("CREATOR PUT", "6");
        ed.commit();
    }

    /**
     * retrieve cat object from SP with given key and default value
     * @param key
     * @param notReceived
     * @return
     */
    public Cat getCatObject(String key, Object notReceived){
        Cat cat;
        Gson gson = new Gson();
        String serialized = gson.toJson(notReceived);
        String json = sp.getString(key, serialized);
        if(json!=serialized){
            cat = gson.fromJson(json,Cat.class);
        }   else{
            cat = new Cat();
            Log.i("CAT_SP","Read null value, returned default value");
        }
        return cat;
    }

    public void putString(String key,String message){
        if(!sp.getString(key,"").equals("")){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w. message: "+sp.getString(key,""));
        }
        ed = sp.edit();
        ed.putString(key,message);
        ed.commit();
    }


    public String getString(String key, String notReceived){
        return sp.getString(key,notReceived);
    }

    public void putInt(String key,int number){
        if(sp.getInt(key,0)!=0){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w.message: "+sp.getInt(key, 0));
        }
        ed = sp.edit();
        ed.putInt(key, number);
        ed.commit();
    }

    public int getInt(String key, int notReceived){
        return sp.getInt(key, notReceived);
    }

    public void putFloat(String key,float number){
        if(sp.getFloat(key, 0)!=0){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w.message: "+sp.getFloat(key, 0));
        }
        ed = sp.edit();
        ed.putFloat(key, number);
        ed.commit();
    }
    public float getFloat(String key, float notReceived){
        return sp.getFloat(key, notReceived);
    }

    public void putDouble(String key,double number){
        if(sp.getFloat(key, 0)!=0){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w.message: "+sp.getFloat(key, 0));
        }
        float floatFromDouble = Float.parseFloat(Double.toString(number));
        ed = sp.edit();
        ed.putFloat(key, floatFromDouble );
        ed.commit();
    }

    public double getDouble(String key, double notReceived){
        float floatFromDouble = Float.parseFloat(Double.toString(notReceived));
        float toDouble = sp.getFloat(key,floatFromDouble);
        return Double.parseDouble(String.valueOf(toDouble));
    }

    public void putBoolean(String key,Boolean message){
        if(!sp.getString(key,"").equals("")){
            Log.i("SHARED PREFERENCES","not null w. key "+key+" and w. message: "+sp.getBoolean(key, false));
        }
        ed = sp.edit();
        ed.putBoolean(key, message);
        ed.commit();
    }

    public Boolean getBoolean(String key, Boolean notReceived){
        return sp.getBoolean(key, notReceived);
    }


}
