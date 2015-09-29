package geofencing;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import backgroundcat.CatNotifications;
import cat.Constants;
import cat.Tags;
import controllers.SharedPreferencesController;
import controllers.SettingsController;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class GeofencesIntentService extends IntentService {
    private SettingsController sc;
    private SharedPreferencesController scp;
    /**
     * increment is fixed for now
     */
    private static final double INCREMENT = 10d;

    public GeofencesIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        scp = new SharedPreferencesController(getApplicationContext());
        if(geofencingEvent.hasError()){
            Log.i(Constants.APP_TAG, "Location services error: " + geofencingEvent.getErrorCode());
        } else{
            int transitionType = geofencingEvent.getGeofenceTransition();
            if(Geofence.GEOFENCE_TRANSITION_DWELL == transitionType){
                Log.i("GEOFENCE", "DWELLING INSIDE");
                showOnMainThread(this, "DWELLING INSIDE!");


                sc = new SettingsController(this);
                if(sc.isNotificationPermission()) {
                    CatNotifications.issueNotification(getApplicationContext(), "Good job!", "You are inside! ", Tags.APP_COLOR_SUCCESS);
                }
                Log.i("INCREMENT","SENDING_GOOD_NEWS");
                //not elegant bc only one receiver can receive
                Intent broadcastIntent = new Intent(Tags.SCORE_INCREMENT);
                broadcastIntent.putExtra(Tags.SCORE_INCREMENT_FIELD, INCREMENT);
                getApplicationContext().sendBroadcast(broadcastIntent);
            }
        }
    }

    private void showOnMainThread(final Context context, final String message){
        Handler main = new Handler(Looper.getMainLooper());

        main.post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });
    }


}
