package backgroundcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cat.Cat;
import cat.Tags;
import controllers.SharedPreferencesController;

/**
 * when app is in background, receiver in CatActivity is unregistered, this will provide update of score, put it into current instance
 * Created by JOHANNES on 9/28/2015.
 */
public class ScoreIncrementReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INCREMENT_RECEIVER", "RECEIVED_GOOD_NEWS");
        double increment = intent.getDoubleExtra(Tags.SCORE_INCREMENT_FIELD,0);

        SharedPreferencesController spc = new SharedPreferencesController(context);
        Cat cat = spc.getCatObject(Tags.CURRENT_GAME_INSTANCE,null);
        cat.feedTheArtByValue(context, increment);
        spc.putCat(Tags.CURRENT_GAME_INSTANCE, cat);

        //IntentFilter if = new IntentFilter(new IntentFilter(Tags.SCORE_INCREMENT_FIELD));
        Intent intent2 = new Intent(Tags.SCORE_INCREMENT_FOREGROUND);

        intent2.putExtra(Tags.SCORE_INCREMENT_FIELD,increment);

        //beware of difference between LocalBroadcastManager.getInstance(context).sendBroadcast and context.sendBroadcast
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

    }
}
