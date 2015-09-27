package backgroundcat;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cat.Cat;
import cat.Tags;
import geofencing.Constants;
import geofencing.GeofenceStore;
import geofencing.GeofencesIntentService;
import geofencing.VenueGeofence;

/**
 * Created by JOHANNES on 9/7/2015.
 */
public class FoodLevelUpdateService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String UPDATE_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.notifications.FoodLevelUpdateService";

    Cat cat;
    /**
     * Geofencing variables
     */
    // Internal List of Geofence objects. In a real app, these might be provided by an API based on
    // locations within the user's proximity.
    List<Geofence> mGeofenceList;

    // These will store hard-coded geofences in this sample app.
    private VenueGeofence mRetorykaGeofence;
    private VenueGeofence mMuzeumNarodoweGeofence;
    private VenueGeofence mMuzeumWitrazuGeofence;

    // Persistent storage for geofences.
    private GeofenceStore mGeofenceStorage;

    private LocationServices mLocationService;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;

    public FoodLevelUpdateService(){
        super("FoodLevelUpdateService");
    }


    //starve the cat and broadcast updated food value, put updated cat into sp
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SERVICE", "INTENT HANDLED");



        SharedPreferences sp = getSharedPreferences(Tags.CURRENT_GAME_INFO,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Tags.CURRENT_GAME_INSTANCE,"");
        if(json!=null&&!json.isEmpty()){
            cat = gson.fromJson(json,Cat.class);
        }

        if(cat!=null) {
            cat.updateFoodLevel(getApplicationContext());
            Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
        }

        Broadcasts.send(this, UPDATE_FOODLEVEL_ACTION, cat.getFoodLevel());

        String updatedJson = gson.toJson(cat);
        SharedPreferences.Editor currentInfoEditor= getSharedPreferences(Tags.CURRENT_GAME_INFO, MODE_PRIVATE).edit();
        currentInfoEditor.putString(Tags.CURRENT_GAME_INSTANCE, updatedJson);
        currentInfoEditor.commit();

        /**
         * geofencing magic
         */
        if (!isGooglePlayServicesAvailable()) {
            Log.e(Constants.APP_TAG, "Google Play services unavailable.");
            return;
        }
        mGeofenceStorage = new GeofenceStore(this);
        mGeofenceList = new ArrayList<Geofence>();
        createGeofences(59 * 1000);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();






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


    public void createGeofences(int timeout) {
        // Create internal "flattened" objects containing the geofence data.
        mRetorykaGeofence = new VenueGeofence(
                Constants.RETORYKA_ID,                // geofenceId.
                Constants.RETORYKA_LATITUDE,
                Constants.RETORYKA_LONGITUDE,
                Constants.RETORYKA_RADIUS_METERS,
                timeout,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );
        mMuzeumNarodoweGeofence = new VenueGeofence(
                Constants.MUZEUM_NARODOWE_ID,                // geofenceId.
                Constants.MUZEUM_NARODOWE_LATITUDE,
                Constants.MUZEUM_NARODOWE_LONGITUDE,
                Constants.MUZEUM_NARODOWE_RADIUS_METERS,
                timeout,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        mMuzeumWitrazuGeofence = new VenueGeofence(
                Constants.MUZEUM_WITRAZU_ID,                // geofenceId.
                Constants.MUZEUM_WITRAZU_LATITUDE,
                Constants.MUZEUM_WITRAZU_LONGITUDE,
                Constants.MUZEUM_WITRAZU_RADIUS_METERS,
                timeout,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        // Store these flat versions in SharedPreferences and add them to the geofence list.
        mGeofenceStorage.setGeofence(Constants.RETORYKA_ID, mRetorykaGeofence);
        mGeofenceStorage.setGeofence(Constants.MUZEUM_NARODOWE_ID, mMuzeumNarodoweGeofence);
        mGeofenceStorage.setGeofence(Constants.MUZEUM_WITRAZU_ID, mMuzeumWitrazuGeofence);
        mGeofenceList.add(mRetorykaGeofence.toGeofence());
        mGeofenceList.add(mMuzeumNarodoweGeofence.toGeofence());
        mGeofenceList.add(mMuzeumWitrazuGeofence.toGeofence());
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get the PendingIntent for the geofence monitoring request.
        // Send a request to add the current geofences.
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                mGeofenceRequestIntent).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.i("GEOFENCE","CONNECTED? "+status);
            }
        });
        //Toast.makeText(this, "GEOFENCE SERVICE STARTED", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGeofenceRequestIntent) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Log.i("GEOFENCE","DISCONNECTED? "+status);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If the error has a resolution, start a Google Play services activity to resolve it.
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) getApplicationContext(),
                        Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.e(Constants.APP_TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(Constants.APP_TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }


    /**
     * Checks if Google Play services is available.
     * @return true if it is.
     */
    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            if (Log.isLoggable(Constants.APP_TAG, Log.DEBUG)) {
                Log.d(Constants.APP_TAG, "Google Play services is available.");
            }
            return true;
        } else {
            Log.e(Constants.APP_TAG, "Google Play services is unavailable.");
            return false;
        }
    }

    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofenceRequestIntent != null) {
            return mGeofenceRequestIntent;
        }
        Intent intent = new Intent(this, GeofencesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
