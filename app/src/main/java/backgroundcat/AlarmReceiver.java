package backgroundcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import geofencing.GeofencesSetupIntentService;
import controllers.SettingsController;

/**
 * Created by JOHANNES on 9/8/2015.
 */
public class AlarmReceiver extends BroadcastReceiver{
    public static final int CODE = 1234;
    public static final String TRIGGER = "com.planis.johannes.catprototype.notifications.Alarm";
    @Override
    /**
     * download current settings, start score computingservice with it
     * start geofence in service
     * better for performance in foreground bc
     */
    public void onReceive(Context context, Intent intent) {

        SettingsController sc = new SettingsController(context);
        double coeff = sc.getStarvingSpeed();
        Log.i("COEFF.ALARM", "" + coeff);
        Intent alarmIntent = new Intent(context, FoodLevelUpdateService.class);
        alarmIntent.putExtra("STARVING_SPEED", coeff);
        context.startService(alarmIntent);

        Intent intent2 = new Intent(context, GeofencesSetupIntentService.class);
        context.startService(intent2);
    }
}
