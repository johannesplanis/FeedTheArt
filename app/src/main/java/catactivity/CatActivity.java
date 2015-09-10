package catactivity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.planis.johannes.catprototype.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import backgroundcat.BackgroundAlarmManager;
import backgroundcat.FoodLevelUpdateService;
import cat.Cat;
import cat.Flags;
import menuactivity.MenuActivity;


public class CatActivity extends FragmentActivity implements CatArtFragment.OnRefreshCatArtListener {

    public CatFragment catf;
    public CatArtFragment artf;
    public CatMapFragment mapf;
    public CatSplashFragment splash;
    public Handler handler = new Handler();
    public ArtDownloader ad;
    public ArtObject artObject;
    public JSONObject jsonObject;
    public SharedPreferences prefs;
    public String dir;
    Bitmap bitmap;
    public Cat cat;
    private String name;
    public BackgroundAlarmManager bam;
    private Handler foodHandler;
    private Runnable foodHandlerTask;
    Timer timer;
    public static final int INTERVAL_FOREGROUND = 100;
    public static final int INTERVAL_BACKGROUND = 1000*10;

    public static final String STORAGE_KEY = "SHARED_PREFERENCES_KEY";
    private static final String SEARCH_TERM = "/search.json?wskey=NQc7GcL5M&query=guitar&start=1&rows=24&profile=breadcrumb"; //qf=animals

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        Intent intent = getIntent();
        String startMode = intent.getStringExtra("START_MODE");

        startup(startMode);
        cat = new Cat();

        name = "Czarek";
        loadGameInstance();
        //preloadArt();
        artObject = new ArtObject();
        jsonObject = new JSONObject();
        ad = new ArtDownloader(getApplicationContext());
        bam = new BackgroundAlarmManager(getApplicationContext());
        getJSON(SEARCH_TERM);

        //alternative to timer
        /*foodHandler = new Handler();
        foodHandlerTask = new Runnable() {
            @Override
            public void run() {
                computeInForeground();
                foodHandler.postDelayed(this,INTERVAL_FOREGROUND);
            }
        };
*/
    }

    @Override
    public void onResume(){
        super.onResume();
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
        },0,INTERVAL_FOREGROUND);
        //foodHandlerTask.run();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i("ACTIVITY", "PAUSED");
        //make updates less frequent to save battery but run from time to time
        bam.setupAlarm(INTERVAL_BACKGROUND);
        //foodHandler.removeCallbacks(foodHandlerTask);
        timer.cancel();
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

    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();

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
        getJSON(SEARCH_TERM);

        //need better way to update fragment only after new JSON is returned, callback from onSuccess?
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CatArtFragment catFrag = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
                if(catFrag != null){
                    //update art
                    if (artObject==null){
                        artObject = getFromCache(STORAGE_KEY);
                    }
                    catFrag.updateArt(artObject);

                }   else{
                    Toast.makeText(getApplicationContext(),"Unable to refresh. Fragment not found!",Toast.LENGTH_LONG).show();
                }
            }
        }, 500);

    }
    /*
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
        dir = prefs.getString("CAT_IMAGE","");
        if (!dir.equals("")){
            bitmap =  ad.loadImageFromStorage(dir);
        } else{
            new LoadImage().execute("http://europeanastatic.eu/api/image?uri=http%3A%2F%2Fhdl.handle.net%2F10107%2F1458373-13&size=LARGE&type=IMAGE");
        }
    }

    //read current instance of game, and translate from json into Object
    public void loadGameInstance(){
        SharedPreferences sp = getSharedPreferences(Flags.CURRENT_GAME_INFO,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Flags.CURRENT_GAME_INSTANCE, "");
        if(json!=null&&!json.isEmpty()){
            cat = gson.fromJson(json,Cat.class);
        }
        if(cat!=null) {
            if (cat.getName() != null) {
                Log.i("KITTY", cat.getName());
                this.name = cat.getName();
            }
        }

    }
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

        ft.commit();
    }
    /*
    methods to navigate to fragments
     */

    public void toMenu(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("TYPE", "APP");
        startActivity(intent);
        //finish();

    }
    public void toMap(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
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
    public void toExtra(){


        SharedPreferences sp = getSharedPreferences(Flags.CURRENT_GAME_INFO,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Flags.CURRENT_GAME_INSTANCE, "");
        if(json!=null&&!json.isEmpty()){
            cat = gson.fromJson(json,Cat.class);
        }

        if(cat!=null) {
            cat.feedTheArtByValue(5);
            Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
        }

        Intent broadcastIntent = new Intent(FoodLevelUpdateService.UPDATE_FOODLEVEL_ACTION);
        //int randomInt = (int) (Math.random()*100)+1;
        broadcastIntent.putExtra("SERVICE_BROADCAST",cat.getFoodLevel());
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        String updatedJson = gson.toJson(cat);
        SharedPreferences.Editor currentInfoEditor= getSharedPreferences(Flags.CURRENT_GAME_INFO, MODE_PRIVATE).edit();
        currentInfoEditor.putString(Flags.CURRENT_GAME_INSTANCE, updatedJson);
        currentInfoEditor.commit();

    }

    /*
    sharing art
     */
    public void shareArt(){

    }

    /*
    Asynchronously download today's art
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
    /*
    send REST request, parse response, download image and put everything in ArtObject, cache into SharedPreferences
     */
    public void getJSON(String address){

        ArtDownloadRestClient.get(address, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject
                //System.out.println(response);
                JSONObject item;
                try {
                    JSONArray array = response.getJSONArray("items");
                    //get random search result
                    item = (JSONObject) array.get((int) Math.floor(Math.random() * 24));
                    jsonObject = item;

                    artObject = ad.getArtObjectFromJSON(jsonObject);
                    cacheObject(artObject, STORAGE_KEY);


                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonParser jp = new JsonParser();
                    //JsonElement je = jp.parse(jsonObject);
                    String prettyJson = gson.toJson(jsonObject);

                    System.out.println(prettyJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("object" + item);


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

                // Do something with the response

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject json) {
                Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_LONG).show();
                artObject = getFromCache(STORAGE_KEY);
            }

        });


    }
    public ArtObject getArtObject(){
        if(getSharedPreferences(STORAGE_KEY,MODE_PRIVATE).getString("NAME","").equals("")) {
            System.out.println("From current object in memory");
            return this.artObject;
        } else{
            System.out.println("From cache");
            return getFromCache(STORAGE_KEY);
        }
    }

    // get ArtObject, download bitmap, and put it into storage asynchronously, save object into SharedPreferences
    // commented-out is being executed in async thread
    public boolean cacheObject(ArtObject art, String sharedPreferencesKey){

        try{

            ArtDownloader ad = new ArtDownloader(getApplicationContext());
            new LoadImage().execute(art.getUrl());

            //String path = ad.saveImageToStorage(bitmap);
            SharedPreferences.Editor editor = getSharedPreferences(sharedPreferencesKey,MODE_PRIVATE).edit();
            editor.putString("NAME",art.getName());
            editor.putString("AUTHOR",art.getAuthor());
            editor.putString("URL",art.getUrl());
            //editor.putString("STORAGE_PATH",path);
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
        ArtDownloader ad = new ArtDownloader(getApplicationContext());

        prefs = getSharedPreferences(sharedPreferencesKey,MODE_PRIVATE);
        art.setName(prefs.getString("NAME", ""));
        art.setAuthor(prefs.getString("AUTHOR", ""));
        art.setUrl(prefs.getString("URL", ""));
        art.setStorageUri(prefs.getString("STORAGE_PATH", ""));

        return art;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };
    public String getName(){
        return this.name;
    }

    public void computeInForeground(){

            SharedPreferences sp = getSharedPreferences(Flags.CURRENT_GAME_INFO,MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sp.getString(Flags.CURRENT_GAME_INSTANCE, "");
            if(json!=null&&!json.isEmpty()){
                cat = gson.fromJson(json,Cat.class);
            }

            if(cat!=null) {
                cat.updateFoodLevel(getApplicationContext());
                Log.i("FOOD_LEVEL",String.valueOf(cat.getFoodLevel()));
            }

            Intent broadcastIntent = new Intent(FoodLevelUpdateService.UPDATE_FOODLEVEL_ACTION);
            //int randomInt = (int) (Math.random()*100)+1;
            broadcastIntent.putExtra("SERVICE_BROADCAST",cat.getFoodLevel());
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            String updatedJson = gson.toJson(cat);
            SharedPreferences.Editor currentInfoEditor= getSharedPreferences(Flags.CURRENT_GAME_INFO, MODE_PRIVATE).edit();
            currentInfoEditor.putString(Flags.CURRENT_GAME_INSTANCE, updatedJson);
            currentInfoEditor.commit();



    }




}
