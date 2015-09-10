package backgroundcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by JOHANNES on 9/8/2015.
 */
public class AlarmReceiver extends BroadcastReceiver{
    public static final int CODE = 1234;
    public static final String TRIGGER = "com.planis.johannes.catprototype.notifications.Alarm";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, FoodLevelUpdateService.class);
        context.startService(alarmIntent);
    }
}
