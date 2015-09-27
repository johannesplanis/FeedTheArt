package backgroundcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import catactivity.CatActivity;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BackgroundAlarmManager bam = new BackgroundAlarmManager(context);
        bam.setupAlarm(CatActivity.INTERVAL_BACKGROUND);
        Log.i("FEEDTHEART","Starting work after boot!");
    }
}
