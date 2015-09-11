package backgroundcat;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import cat.Cat;
import cat.Tags;

/**
 * Created by JOHANNES on 9/7/2015.
 */
public class FoodLevelUpdateService extends IntentService {

    public static final String UPDATE_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.notifications.FoodLevelUpdateService";

    Cat cat;
    public FoodLevelUpdateService(){
        super("FoodLevelUpdateService");
    }


    //starve the cat and broadcast updated food value, put updated cat into sp
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("SERVICE","INTENT HANDLED");

        SharedPreferences sp = getSharedPreferences(Tags.CURRENT_GAME_INFO,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Tags.CURRENT_GAME_INSTANCE, "");
        if(json!=null&&!json.isEmpty()){
            cat = gson.fromJson(json,Cat.class);
        }

        if(cat!=null) {
            cat.updateFoodLevel(getApplicationContext());
            Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
        }

        Broadcasts.send(this, UPDATE_FOODLEVEL_ACTION, cat.getFoodLevel());

        String updatedJson = gson.toJson(cat);
        SharedPreferences.Editor currentInfoEditor= getSharedPreferences(Tags.CURRENT_GAME_INFO, MODE_PRIVATE).edit();
        currentInfoEditor.putString(Tags.CURRENT_GAME_INSTANCE, updatedJson);
        currentInfoEditor.commit();




    }


    @Override
    public int onStartCommand(Intent intent, int flags,int startId){
        Log.i("SERVICE","STARTED");
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("SERVICE","DESTROYED");
    }


}
