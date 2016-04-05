package activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.planis.johannes.feedtheart.bambino.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import backgroundcat.BackgroundAlarmManager;
import catactivity.ArtDownloader;
import catactivity.ArtObject;
import controllers.SettingsController;
import controllers.SharedPreferencesController;
import fragments.CatArtFragment;
import fragments.CatFragment;
import fragments.CatSplashFragment;
import geofencing.GeofenceStore;
import geofencing.GeofencesIntentService;
import geofencing.VenueGeofence;
import geofencing.VenueObject;
import geofencing.VenuesDevelopmentMode;
import model.Cat;
import model.Constants;
import model.Tags;

public class CatActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public CatFragment catf;
    public CatArtFragment artf;
    public CatSplashFragment splash;
    public ArtDownloader ad;
    public ArtObject artObject;
    public JSONObject jsonObject;
    public SharedPreferences prefs;
    SharedPreferencesController spc;
    SettingsController sc;

    Bitmap bitmap;
    public Cat cat;
    private String name;
    public BackgroundAlarmManager bam;

    public double starvingSpeed;


    Timer timer;

    public static final String STORAGE_KEY = "SHARED_PREFERENCES_KEY";

    /**
     * Geofencing variables
     */
    // Internal List of Geofence objects. In a real app, these might be provided by an API based on
    // locations within the user's proximity.
    List<Geofence> mGeofenceList;

    // These will store hard-coded geofences in this sample app.


    // Persistent storage for geofences.
    private GeofenceStore mGeofenceStorage;
    public ArrayList<VenueGeofence> vg;
    public ArrayList<VenueObject> vo;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        checkPermissions();

        Intent intent = getIntent();
        String startMode = intent.getStringExtra("START_MODE");


        //Setup controllers
        spc = new SharedPreferencesController(getApplicationContext());
        sc = new SettingsController(getApplicationContext());
        //Setup settings
        updateSettings();
        //load cat from sp
        loadGameInstance();
        //setup view
        startup();

        artObject = new ArtObject();
        ad = new ArtDownloader(getApplicationContext());
        bam = new BackgroundAlarmManager(getApplicationContext());

    }
    private static final int CAT = 1;
    private static final int ART = 2;

    private int fragmentType = CAT;

    private android.app.Fragment getFragment(){
        switch (fragmentType){
            case CAT:
                return new CatFragment();
            case ART:
                return new CatArtFragment();
            default:
                return new CatFragment();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            }


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else{
                    finish();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //register broadcast receiver on increments
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, new IntentFilter(Tags.SCORE_INCREMENT_FOREGROUND));

        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .registerReceiver(humourBroadcastReceiver, new IntentFilter("HUMOUR"));
        spc = new SharedPreferencesController(getApplicationContext());
        loadGameInstance();
        Log.i("ACTIVITY", "RESUMED");
        //make updates more frequent to make score dynamically change in UI
        bam.cancelAlarm();
        //while in foreground, score is computed more frequently to show it in UI. Due to Lollipop restrictions, alarm cannot run more frequently than once a minute
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                computeInForeground();
            }
        }, 0, Constants.INTERVAL_FOREGROUND);


        //setup geofence

        geofenceInit();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .unregisterReceiver(humourBroadcastReceiver);

        timer.cancel();
        saveGameInstance();
        //make updates less frequent to save battery but run from time to time
        bam.setupAlarm(Constants.INTERVAL_BACKGROUND);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LocationServices.GeofencingApi.removeGeofences(
                        mApiClient,
                        // This is the same pending intent that was used in addGeofences().
                        getGeofenceTransitionPendingIntent()
                ).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i("GEOFENCE STATUS", "DISCONNECTED? " + status);
                    }
                }); // Result processed in onResult().
            }
        }, 1000);

    }



    @Override
    public void onBackPressed() {

     switch (fragmentType){
         case ART:
             fragmentType = CAT;
             changeFragment();
             break;
         default:
             finish();
             break;
     }
    }



    /**
     startup methods, navigate to cat
     */
    public void startup() {

        getFragmentManager().beginTransaction().add(R.id.cat_container, new CatFragment()).commit();

    }


    //read current instance of game, and translate from json into Object
    public void loadGameInstance() {
        spc = new SharedPreferencesController(getApplicationContext());
        spc.getCatObject(Tags.CURRENT_GAME_INSTANCE, null);
        cat = spc.getCatObject(Tags.CURRENT_GAME_INSTANCE, null);
        if (cat != null) {
            if (cat.getName() != null) {
                Log.i("KITTY", cat.getName());
                this.name = cat.getName();
            }
        }
    }

    private void updateSettings() {
        this.starvingSpeed = sc.getStarvingSpeed();
    }

    public void saveGameInstance() {
        Cat helperCat = cat;
        spc.putCat(Tags.CURRENT_GAME_INSTANCE, helperCat);
    }



    public void toCatFromArt() {
        fragmentType = CAT;
        changeFragment();

    }

    public void toMenu() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    public void toArt() {

        fragmentType = ART;
        changeFragment();

    }

    private void changeFragment() {
        getFragmentManager().beginTransaction().replace(R.id.cat_container,getFragment()).commit();
    }

    public void toExtra() {
        //run in separate thread
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double food = 5d;
                cat.feedTheArtByValue(getApplicationContext(), food);
                Toast.makeText(getApplicationContext(), "You fed " + cat.getName() + " by " + food + " !", Toast.LENGTH_SHORT).show();

            }
        });
        Log.i("SETTINGS", "COEFF: " + String.valueOf(new SettingsController(this).getStarvingSpeed()));
    }


    /**
     * Asynchronously download today's art
     */
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = ad.getBitmapFromDirectURL(args[0]);
                System.out.println("File downloaded!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (bitmap != null) {
                String path = ad.saveImageToStorage(bitmap);
                SharedPreferences.Editor editor = getSharedPreferences(STORAGE_KEY, MODE_PRIVATE).edit();
                editor.putString("STORAGE_PATH", path);
                editor.commit();
                System.out.println("Saved image asynchronously!");
            }
        }
    }

    public ArtObject getArtObject() {
        spc = new SharedPreferencesController(getApplicationContext());

        if (spc.getArtObject(Tags.ART_CACHE, null) == null) {
            System.out.println("From current object in memory");
            return this.artObject;
        } else {
            System.out.println("From cache");
            spc = new SharedPreferencesController(getApplicationContext());
            return spc.getArtObject(Tags.ART_CACHE, null);
        }
    }




    /**
     * receive increment from geofences while in foreground
     * update foodlevel, broadcast to UI
     *
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //get increment, update cat score here
            double increment = intent.getDoubleExtra(Tags.SCORE_INCREMENT_FIELD, 0);
            String venueID = intent.getStringExtra(Tags.REQ_ID);
            if (venueID != null) {
                Log.i("GEOFENCES_RECEIVER", "" + venueID); //get value of geofence id not equal to 0
                //catDialogTop.setText("" + venueID);
                Intent intent1 = new Intent(Tags.REQ_ID);
                intent1.putExtra(Tags.REQ_ID, venueID);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

            } else {
                Log.i("GEOFENCES_RECEIVER", "null output");
            }
            cat.feedTheArtByValue(getApplicationContext(), increment);
            Log.i("broadcast receiver", "current score " + cat.getFoodLevel());


        }
    };

    /**
     * broadcast receiver to change layout according to changing humour
     */
    private BroadcastReceiver humourBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //int humour = intent.getIntExtra("HUMOUR", 0);
            //cat.setHumour(humour);
        }
    };

    public String getName() {
        return this.name;
    }

    public int getCharacter() {
        return cat.getCharacter();
    }




    /**
     * used in resumed activity
     * after loading cat, used to compute periodically
     */
    public void computeInForeground() {

        if (cat != null) {

            cat.updateFoodLevel(getApplicationContext(), starvingSpeed);
            Log.i("FOOD_LEVEL", String.valueOf(cat.getFoodLevel()));
        }
        Intent broadcastIntent = new Intent(Tags.UPDATE_FOODLEVEL_ACTION);
        broadcastIntent.putExtra("SERVICE_BROADCAST", cat.getFoodLevel());
        broadcastIntent.putExtra("STARVING_SPEED", starvingSpeed);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    /**
     * GEOFENCING section
     */


    public void createGeofences() {

        vg = VenuesDevelopmentMode.sampleVenueGeofences(Geofence.NEVER_EXPIRE);
        for (VenueGeofence v : vg) {
            mGeofenceStorage.setGeofence(v);
        }
        mGeofenceList = VenuesDevelopmentMode.sampleGeofences(Geofence.NEVER_EXPIRE);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get the PendingIntent for the geofence monitoring request.
        // Send a request to add the current geofences.
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                mGeofenceRequestIntent).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.i("GEOFENCES", "CONNECTED? " + status);
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
                    Log.i("GEOFENCE", "DISCONNECTED? " + status);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If the error has a resolution, start a Google Play services activity to resolve it.
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
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
        Intent intent = new Intent(this, GeofencesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    /**
     * setup geofence
     * sometimes produces error when app wasn't properly closed, and services ain't registered yet
     */
    public void geofenceInit(){
        /**
         * geofencing magic
         */
        if (!isGooglePlayServicesAvailable()) {
            Log.e(Constants.APP_TAG, "Google Play services unavailable.");
            return;
        }
        mGeofenceStorage = new GeofenceStore(this);
        mGeofenceList = new ArrayList<Geofence>();
        createGeofences();
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    public double getResult(){
        return cat.getFoodLevel();
    }
    public void putResult(double result){
        cat.setFoodLevel(result);
    }
}
