package backgroundcat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import cat.Cat;
import cat.Tags;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class ScoreUpdater {

    static public void update(Context context, double increment){
        Cat cat = new Cat();
        SharedPreferences sp = context.getSharedPreferences(Tags.CURRENT_GAME_INFO, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Tags.CURRENT_GAME_INSTANCE, "");
        if(json!=null&&!json.isEmpty()){
            cat = gson.fromJson(json, Cat.class);
        }

        if(cat!=null) {
            cat.feedTheArtByValue(increment);
            Log.i("FOOD_LEVEL", String.valueOf(cat.getFoodLevel()));
        }

        Intent broadcastIntent = new Intent(FoodLevelUpdateService.UPDATE_FOODLEVEL_ACTION);
        //int randomInt = (int) (Math.random()*100)+1;
        broadcastIntent.putExtra("SERVICE_BROADCAST",cat.getFoodLevel());
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

        String updatedJson = gson.toJson(cat);
        SharedPreferences.Editor currentInfoEditor= context.getSharedPreferences(Tags.CURRENT_GAME_INFO, Context.MODE_PRIVATE).edit();
        currentInfoEditor.putString(Tags.CURRENT_GAME_INSTANCE, updatedJson);
        currentInfoEditor.commit();
    }
}
