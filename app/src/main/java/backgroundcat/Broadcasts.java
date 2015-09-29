package backgroundcat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 *
 * Created by JOHANNES on 9/11/2015.
 */
public class Broadcasts {

    static public void send(Context context, String intentFilter, String message){
        Intent broadcastIntent = new Intent(intentFilter);
        broadcastIntent.putExtra("SERVICE_BROADCAST",message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
    static public void send(Context context, String intentFilter, double message){
        Intent broadcastIntent = new Intent(intentFilter);
        broadcastIntent.putExtra("SERVICE_BROADCAST",message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
