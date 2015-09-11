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
import backgroundcat.ScoreUpdater;
import cat.Tags;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class GeofencesIntentService extends IntentService {

    public GeofencesIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.i(Constants.APP_TAG, "Location services error: " + geofencingEvent.getErrorCode());
        } else{
            int transitionType = geofencingEvent.getGeofenceTransition();
            if(Geofence.GEOFENCE_TRANSITION_DWELL == transitionType){
                Log.i("GEOFENCE", "DWELLING INSIDE");
                showOnMainThread(this, "DWELLING INSIDE!");
                CatNotifications.issueNotification(getApplicationContext(), "Good job!", "You are inside! ", Tags.APP_COLOR_SUCCESS);
                ScoreUpdater.update(getApplicationContext(), 10);
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
