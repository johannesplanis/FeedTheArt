package backgroundcat;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import cat.Cat;
import cat.Tags;
import controllers.SharedPreferencesController;

/**
 * used to compute food level while in background
 * takes object from SP, computes with given settings and coeff, puts into SP again
 * Created by JOHANNES on 9/7/2015.
 */
public class FoodLevelUpdateService extends IntentService{

    private SharedPreferencesController spc;
    private Cat cat;

    public FoodLevelUpdateService(){
        super("FoodLevelUpdateService");
    }

    //starve the cat and broadcast updated food value, put updated cat into sp
    @Override
    protected void onHandleIntent(Intent intent) {
        spc = new SharedPreferencesController(getApplicationContext());
        Log.i("SERVICE", "INTENT HANDLED");

        double coeff = intent.getDoubleExtra("STARVING_SPEED",0.5);
        Log.i("STARVING COEFF",""+coeff);

        cat = spc.getCatObject(Tags.CURRENT_GAME_INSTANCE, null);

        if(intent.getBooleanExtra("SCORE_UPDATE",false)){
            double score = intent.getDoubleExtra("SCORE",cat.getFoodLevel());
            cat.setFoodLevel(score);
        }
        if(cat!=null) {
            cat.updateFoodLevel(getApplicationContext(),coeff);
            Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
        }

        spc.putCat(Tags.CURRENT_GAME_INSTANCE, cat);
    }


    @Override
    public int onStartCommand(Intent intent, int flags,int startId){
        Log.i("SERVICE","STARTED");
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SERVICE", "DESTROYED");

    }



}
