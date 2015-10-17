package backgroundcat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * class designed to deal with setting up and modifying Alarms inside the application
 *
 */
public class BackgroundAlarmManager {

    Context context;
    public BackgroundAlarmManager(Context context){
        this.context = context;
    }

    //if pendingIntent associated with alarm is not null, there is an alarm present already, don't recreate
    //setup interval for service to run
    public void setupAlarm(int interval){

        Intent intent = new Intent(context, AlarmReceiver.class);
        //intent.putExtra("SETUP_GEOFENCE",setupGeofence);
        if (PendingIntent.getBroadcast(context, AlarmReceiver.CODE, intent, PendingIntent.FLAG_NO_CREATE)==null) {

            final PendingIntent pIntent =PendingIntent.getBroadcast(context, AlarmReceiver.CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //long firstTime = System.currentTimeMillis();

            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+100, interval, pIntent);
            Log.i("ALARM", "STARTED");
        } else{
            Log.i("ALARM","ALREADY STARTED");
        }
    }
    //if alarm is existing, cancel it, cancel Pending Intent associated with it
    public void cancelAlarm(){
        Intent intent = new Intent(context,AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context,AlarmReceiver.CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (PendingIntent.getBroadcast(context,AlarmReceiver.CODE,intent,PendingIntent.FLAG_NO_CREATE)!=null) {
            alarm.cancel(pIntent);
            if (pIntent != null) {
                pIntent.cancel();
            }
            Log.i("ALARM", "CANCELLED");

        }   else{
            Log.i("ALARM","NOT EXISTENT");
        }

    }

    public void resetAlarm(int newInterval){
        cancelAlarm();
        setupAlarm(newInterval);
    }
}
