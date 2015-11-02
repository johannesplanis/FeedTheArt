package catactivity;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.planis.johannes.feedtheart.bambino.R;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import backgroundcat.BackgroundAlarmManager;
import cat.Cat;
import cat.Constants;
import cat.Tags;
import controllers.BitmapController;
import controllers.SettingsController;
import controllers.SharedPreferencesController;
import geofencing.GeofenceStore;
import geofencing.GeofencesIntentService;
import geofencing.VenueGeofence;
import geofencing.VenueObject;
import geofencing.VenuesDevelopmentMode;
import menuactivity.MenuActivity;

public class CatActivity extends FragmentActivity implements CatArtFragment.OnRefreshCatArtListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public CatFragment catf;
    public CatArtFragment artf;
    public CatMapFragment mapf;
    public CatSplashFragment splash;
    public Handler handler = new Handler();
    public ArtDownloader ad;
    public ArtObject artObject;
    public JSONObject jsonObject;
    public SharedPreferences prefs;
    SharedPreferencesController spc;
    SettingsController sc;
    BitmapController bc;
    public String dir;
    Bitmap bitmap;
    public Cat cat;
    private String name;
    public BackgroundAlarmManager bam;
    private Handler foodHandler;
    private Runnable foodHandlerTask;

    public double starvingSpeed;


    Timer timer;
    Timer foregroundGeofenceTimer;
    public static final int INTERVAL_FOREGROUND = 500;
    public static final int INTERVAL_BACKGROUND = 1000*10;

    public static final String STORAGE_KEY = "SHARED_PREFERENCES_KEY";
    private static final String SEARCH_TERM = "/search.json?wskey=NQc7GcL5M&query=guitar&start=1&rows=24&profile=breadcrumb"; //qf=animals

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
    private VenueGeofence mMicroscupGeofence;
    private VenueGeofence mPNGeofence;
    private VenueGeofence mMakowaGeofence;
    private VenueGeofence mAuditoriumMaximumGeofence;

    // Persistent storage for geofences.
    private GeofenceStore mGeofenceStorage;
    public ArrayList<VenueGeofence> vg;
    public ArrayList<VenueObject> vo;
    private LocationServices mLocationService;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;
    private enum REQUEST_TYPE {ADD}
    private REQUEST_TYPE mRequestType;
    JSONObject artJSON;

    public ArrayList<VenueObject> getVo() {
        return vo;
    }

    public void setVo(ArrayList<VenueObject> vo) {
        this.vo = vo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

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
        startup(startMode);

        artObject = new ArtObject();
        jsonObject = new JSONObject();
        ad = new ArtDownloader(getApplicationContext());
        bam = new BackgroundAlarmManager(getApplicationContext());

        getJSON(Constants.relativeApiUrl);
        //VenueStorageController vsc = new VenueStorageController();
        //vo = VenuesDevelopmentMode.sampleVenues();
        //vsc.putVenues(getApplicationContext(),vo);


    }

    @Override
    public void onResume(){
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
    public void onPause(){
        super.onPause();
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .unregisterReceiver(humourBroadcastReceiver);

        Log.i("ACTIVITY", "PAUSED");
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
        }, 500);

    }

    @Override
    protected void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);


    }
    /*
    handling orientation changes
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

    }

    //najpierw wyczyść back stack, potem wyłącz
    //ma powodować kliknij drugi raz żeby wyjść
    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();
        /**
         * if you press back from art, feed the cat
         *
         */
        artf = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
        if(artf!=null&&!artf.isHidden()){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    double food = 10d;
                    cat.feedTheArtByValue(getApplicationContext(),food);
                    Toast.makeText(getApplicationContext(),"You fed "+cat.getName()+" by "+food+" !", Toast.LENGTH_SHORT).show();
                }
            });

        }


        if (count == 0) {
            //go to main activity and exit
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    //implement callback from cat fragment
    @Override
    public void onRefreshSelected(){
        //download new JSON, parse, return ArtObject, call fragment updater function
        getJSON(Constants.relativeApiUrl);

        //need better way to update fragment only after new JSON is returned, callback from onSuccess?
        final Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CatArtFragment catFrag = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
                if (catFrag != null) {
                    //update art
                    if (artObject == null) {
                        spc = new SharedPreferencesController(getApplicationContext());
                        artObject = spc.getArtObject(Tags.ART_CACHE, null);
                        //artObject = getFromCache(STORAGE_KEY);
                    }
                    catFrag.updateArt(artObject);

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to refreshSettings. Fragment not found!", Toast.LENGTH_LONG).show();
                }
            }
        }, 500);
    }


    /**
    startup methods, navigate to cat
     */
    public void startup(String startMode){

        if(startMode.equals("NOTIFICATION")){
            toCat();
        } else{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            splash = new CatSplashFragment();

            if(splash.isAdded()){
                ft.show(splash);
            } else{
                ft.add(R.id.cat_container, splash, "SPLASH");
            }
            ft.commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toCat();
                }
            }, 1000);
        }
    }

    /*
    preload daily art either from web, or internal storage, currently not in use
     */
    public void preloadArt(){
        ad = new ArtDownloader(getApplicationContext());
        prefs = getSharedPreferences("STORAGE_MANAGER", MODE_PRIVATE);
        dir = prefs.getString("CAT_IMAGE", "");
        if (!dir.equals("")){
            bitmap =  ad.loadImageFromStorage(dir);
        } else{
            new LoadImage().execute("http://europeanastatic.eu/api/image?uri=http%3A%2F%2Fhdl.handle.net%2F10107%2F1458373-13&size=LARGE&type=IMAGE");
        }
    }

    //read current instance of game, and translate from json into Object
    public void loadGameInstance(){
        spc = new SharedPreferencesController(getApplicationContext());
        spc.getCatObject(Tags.CURRENT_GAME_INSTANCE,null);
        cat = spc.getCatObject(Tags.CURRENT_GAME_INSTANCE, null);
        if(cat!=null) {
            if (cat.getName() != null) {
                Log.i("KITTY", cat.getName());
                this.name = cat.getName();
            }
        }
    }

    private void updateSettings(){
        this.starvingSpeed = sc.getStarvingSpeed();
    }

    public void saveGameInstance(){
        Cat helperCat = cat;
        spc.putCat(Tags.CURRENT_GAME_INSTANCE, helperCat);
    }


    @Deprecated
    public void loadBitmap(ImageView imageView){
        int character = cat.getCharacter();
        int resId = Constants.catImageResIds[character];
        Log.i("CAT FRAGMENT", "character: " + character);

        Picasso.with(this).load(resId).into(imageView);


        //bc = new BitmapController(getApplicationContext());
       // bc.loadBitmap(resId, imageView, placeholderBitmap);
    }

    /**
     *
     * Navigation methods
     *
    */
    /**
     *
     */
    public void toCat(){



        FragmentTransaction ft = getFragmentManager().beginTransaction();
        catf = new CatFragment();

        if(catf.isAdded()){
            ft.show(catf);
        } else{
            ft.add(R.id.cat_container, catf, "CATF");
        }

        mapf = (CatMapFragment) getFragmentManager().findFragmentByTag("MAPF");
        if(mapf!=null){
            ft.hide(mapf);
        }


        artf = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
        if(artf!=null){
            ft.hide(artf);
        }

        splash = (CatSplashFragment) getFragmentManager().findFragmentByTag("SPLASH");
        if(splash!=null){
            ft.hide(splash);
        }

        ft.commit();
    }

    public void toCatFromArt(){
        //
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double food = 10d;
                cat.feedTheArtByValue(getApplicationContext(),food);
                Toast.makeText(getApplicationContext(),"You fed "+cat.getName()+" by "+food+" !", Toast.LENGTH_SHORT).show();
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        catf = new CatFragment();

        if(catf.isAdded()){
            ft.show(catf);
        } else{
            ft.add(R.id.cat_container, catf, "CATF");
        }

        artf = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
        if(artf!=null){
            ft.hide(artf);
        }

        ft.commit();

    }
    /**
    methods to navigate to fragments
     */

    /**
     * navigate to menu activity
     */
    public void toMenu(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("TYPE", "APP");
        startActivity(intent);
        //finish();

    }

    /**
     * navigate to map
     */
    public void toMap(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //android.support.v4.app.FragmentTransaction sft = getSupportFragmentManager().beginTransaction();
        mapf = new CatMapFragment();
        if(mapf.isAdded()){
            ft.show(mapf);
        } else{
            ft.add(R.id.cat_container, mapf, "MAPF");
        }
        catf = (CatFragment) getFragmentManager().findFragmentByTag("CATF");
        if(catf!=null){
            ft.hide(catf);
        }
        ft.addToBackStack("MAP");
        ft.commit();
    }

    /**
     * navigate to art
     */
    public void toArt(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        artf = new CatArtFragment();
        if(artf.isAdded()){
            ft.show(artf);
        } else{
            ft.add(R.id.cat_container, artf, "ARTF");
        }
        catf = (CatFragment) getFragmentManager().findFragmentByTag("CATF");
        if(catf!=null){
            ft.hide(catf);
        }
        ft.addToBackStack("ART");
        ft.commit();
    }

    //TEST
    //increment food level by some value

    /**
     * navigate to extra
     * currently used to test incrementing of score
     */
    public void toExtra(){
        //run in separate thread
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double food = 5d;
                cat.feedTheArtByValue(getApplicationContext(),food);
                Toast.makeText(getApplicationContext(),"You fed "+cat.getName()+" by "+food+" !", Toast.LENGTH_SHORT).show();

            }
        });
        Log.i("SETTINGS", "COEFF: " + String.valueOf(new SettingsController(this).getStarvingSpeed()));
    }

    /*
    sharing art
     */
    public void shareArt(){
    }

    /**
     * Asynchronously download today's art
     */
    private class LoadImage extends AsyncTask<String, String, Bitmap>{
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = ad.getBitmapFromDirectURL(args[0]);
                System.out.println("File downloaded!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image){
            if(bitmap!=null) {
                String path = ad.saveImageToStorage(bitmap);
                SharedPreferences.Editor editor = getSharedPreferences(STORAGE_KEY,MODE_PRIVATE).edit();
                editor.putString("STORAGE_PATH",path);
                editor.commit();
                System.out.println("Saved image asynchronously!");
            }
        }
    }
    /**
     * send REST request, parse response, download image and put everything in ArtObject, cache into SharedPreferences
     */
    public void getJSON(String address){

        final SharedPreferencesController spcLocal = new SharedPreferencesController(getApplicationContext());
        ArtDownloadRestClient.get(address, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                try {
                    String url = response.getString("image_url");
                    Log.i("JSON OBJECT URL",""+url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //new API
                try {
                    artJSON = response.getJSONObject("dailyart");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString = artJSON.toString();
                artObject = gson.fromJson(jsonString,ArtObject.class);

                /* old API
                String jsonString = response.toString();
                artObject = gson.fromJson(jsonString,ArtObject.class);
                */

                spcLocal.putArt(Tags.ART_CACHE, artObject);
                @Deprecated
                //cacheObject(artObject, STORAGE_KEY);
                String prettyJson = gson.toJson(response);
                Log.i("REST Api",prettyJson);
                Log.i("ART OBJECT", ""+jsonString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
                try {
                    JSONObject res = (JSONObject) response.get(0);
                    System.out.println("array" + res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject json) {
                Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_SHORT).show();
                spc = new SharedPreferencesController(getApplicationContext());
                artObject = spc.getArtObject(Tags.ART_CACHE, null);

                //artObject = getFromCache(STORAGE_KEY);
            }
        });

    }


    public ArtObject getArtObject(){
        spc = new SharedPreferencesController(getApplicationContext());

        if(spc.getArtObject(Tags.ART_CACHE,null)==null) {
            System.out.println("From current object in memory");
            return this.artObject;
        } else{
            System.out.println("From cache");
            spc = new SharedPreferencesController(getApplicationContext());
            return spc.getArtObject(Tags.ART_CACHE, null);
        }
    }

    // get ArtObject, download bitmap, and put it into storage asynchronously, save object into SharedPreferences
    // commented-out is being executed in async thread
    public boolean cacheObject(ArtObject art, String sharedPreferencesKey){

        try{

            new LoadImage().execute(art.getImage_url());

            SharedPreferences.Editor editor = getSharedPreferences(sharedPreferencesKey,MODE_PRIVATE).edit();
            editor.putString("NAME",art.getName());
            editor.putString("AUTHOR",art.getAuthor());
            editor.putString("URL",art.getImage_url());
            editor.commit();

            System.out.println("Saved object data synchronously!");
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("cache unsuccesful");
            return false;
        }
        return true;
    }
    public ArtObject getFromCache(String sharedPreferencesKey){
        ArtObject art = new ArtObject();


        prefs = getSharedPreferences(sharedPreferencesKey,MODE_PRIVATE);
        art.setName(prefs.getString("NAME", ""));
        art.setAuthor(prefs.getString("AUTHOR", ""));
        art.setImage_url(prefs.getString("URL", ""));
        art.setStorageUri(prefs.getString("STORAGE_PATH", ""));

        return art;
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
            if(venueID!=null) {
                Log.i("GEOFENCES_RECEIVER", "" + venueID); //get value of geofence id not equal to 0
                //catDialogTop.setText("" + venueID);
                Intent intent1 = new Intent(Tags.REQ_ID);
                intent1.putExtra(Tags.REQ_ID,venueID);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

            } else{
                Log.i("GEOFENCES_RECEIVER","null output");
            }
            cat.feedTheArtByValue(getApplicationContext(),increment);
            Log.i("broadcast receiver","current score "+cat.getFoodLevel());


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

    public String getName(){
        return this.name;
    }

    public int getCharacter(){
        return cat.getCharacter();
    }

    public int getHumour(){return cat.getHumour();}
    /**
    * SCORE computing section
    */


    /**
     * used in resumed activity
     * after loading cat, used to compute periodically
     */
    public void computeInForeground(){

            if(cat!=null) {

                cat.updateFoodLevel(getApplicationContext(),starvingSpeed);
                Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
            }
            Intent broadcastIntent = new Intent(Tags.UPDATE_FOODLEVEL_ACTION);
            broadcastIntent.putExtra("SERVICE_BROADCAST",cat.getFoodLevel());
            broadcastIntent.putExtra("STARVING_SPEED", starvingSpeed);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    /**
     * GEOFENCING section
     */


    public void createGeofences() {

        /*
        // Create internal "flattened" objects containing the geofence data.
        mRetorykaGeofence = new VenueGeofence(
                Constants.RETORYKA_ID,                // geofenceId.
                Constants.RETORYKA_LATITUDE,
                Constants.RETORYKA_LONGITUDE,
                Constants.RETORYKA_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );
        mMuzeumNarodoweGeofence = new VenueGeofence(
                Constants.MUZEUM_NARODOWE_ID,                // geofenceId.
                Constants.MUZEUM_NARODOWE_LATITUDE,
                Constants.MUZEUM_NARODOWE_LONGITUDE,
                Constants.MUZEUM_NARODOWE_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        mMuzeumWitrazuGeofence = new VenueGeofence(
                Constants.MUZEUM_WITRAZU_ID,                // geofenceId.
                Constants.MUZEUM_WITRAZU_LATITUDE,
                Constants.MUZEUM_WITRAZU_LONGITUDE,
                Constants.MUZEUM_WITRAZU_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );
        mPNGeofence = new VenueGeofence(
                Constants.PN_ID,
                Constants.PN_LATITUDE,
                Constants.PN_LONGITUDE,
                Constants.PN_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL);

        mMakowaGeofence = new VenueGeofence(
                Constants.MAKOWA_ID,                // geofenceId.
                Constants.MAKOWA_LATITUDE,
                Constants.MAKOWA_LONGITUDE,
                Constants.MAKOWA_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        mMicroscupGeofence = new VenueGeofence(
                Constants.MICROSCUP_ID,
                Constants.MICROSCUP_LATITUDE,
                Constants.MICROSCUP_LONGITUDE,
                Constants.MICROSCUP_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        mAuditoriumMaximumGeofence = new VenueGeofence(
                Constants.AUDITORIUM_MAXIMUM_ID,
                Constants.AUDITORIUM_MAXIMUM_LATITUDE,
                Constants.AUDITORIUM_MAXIMUM_LONGITUDE,
                Constants.AUDITORIUM_MAXIMUM_RADIUS_METERS,
                Geofence.NEVER_EXPIRE,
                Geofence.GEOFENCE_TRANSITION_DWELL
        );

        // Store these flat versions in SharedPreferences and add them to the geofence list.
        mGeofenceStorage.setGeofence(mRetorykaGeofence);
        mGeofenceStorage.setGeofence(mMuzeumNarodoweGeofence);
        mGeofenceStorage.setGeofence(mMuzeumWitrazuGeofence);
        mGeofenceStorage.setGeofence(mMicroscupGeofence);
        mGeofenceStorage.setGeofence(mPNGeofence);
        mGeofenceStorage.setGeofence(mMakowaGeofence);
        mGeofenceStorage.setGeofence(mAuditoriumMaximumGeofence);

        mGeofenceList.add(mRetorykaGeofence.toGeofence());
        mGeofenceList.add(mMuzeumNarodoweGeofence.toGeofence());
        mGeofenceList.add(mMuzeumWitrazuGeofence.toGeofence());
        mGeofenceList.add(mMicroscupGeofence.toGeofence());
        mGeofenceList.add(mPNGeofence.toGeofence());
        mGeofenceList.add(mMakowaGeofence.toGeofence());
        mGeofenceList.add(mAuditoriumMaximumGeofence.toGeofence());
        */
        vg = VenuesDevelopmentMode.sampleVenueGeofences(Geofence.NEVER_EXPIRE);
        for(VenueGeofence v:vg){
            mGeofenceStorage.setGeofence(v);
        }
        mGeofenceList = VenuesDevelopmentMode.sampleGeofences(Geofence.NEVER_EXPIRE);
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
