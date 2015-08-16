package catactivity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import menuactivity.MenuActivity;
import menuactivity.SplashFragment;


public class CatActivity extends Activity implements CatArtFragment.OnRefreshCatArtListener {

    public CatFragment catf;
    public CatArtFragment artf;
    public CatMapFragment mapf;
    public SplashFragment splash;
    public Handler handler = new Handler();
    public ArtDownloader ad;
    public ArtObject artObject;
    public JSONObject jsonObject;
    public SharedPreferences prefs;
    public String dir;
    Bitmap bitmap;

    public static final String STORAGE_KEY = "SHARED_PREFERENCES_KEY";
    private static final String SEARCH_TERM = "/search.json?wskey=NQc7GcL5M&query=guitar&start=1&rows=24&profile=breadcrumb"; //qf=animals

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        startup();
        //preloadArt();
        artObject = new ArtObject();
        jsonObject = new JSONObject();
        ad = new ArtDownloader(getApplicationContext());

        getJSON(SEARCH_TERM);


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
    public void startup(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        splash = new SplashFragment();

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
    public void toExtra(){

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

        ArtDownloadRestClient.get(address,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject
                //System.out.println(response);
                JSONObject item;
                try {
                    JSONArray array = response.getJSONArray("items");
                    //get random search result
                    item = (JSONObject) array.get((int) Math.floor(Math.random()*24));
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
        art.setStorageUri(prefs.getString("STORAGE_PATH",""));

        return art;
    }
}
