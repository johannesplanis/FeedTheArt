package catactivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by JOHANNES on 8/13/2015.
 */
public class ArtDownloader {
    /*
    ISSUES:
     image size
     image not found
     internal/external storage
     */
    Context context;
    public ArtDownloader(Context context){
        this.context = context;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    //create new ArtObject, fill with fields read from jsonObject, read url of preview image
    //download image based on the url !!!
    //add image to ArtObject
    //catch all cases associated with incomplete JSON response
    public ArtObject getArtObjectFromJSON(JSONObject jsonObject){
        ArtObject artObject = new ArtObject();
        try {
            String url = stripBackslashAndBraces(jsonObject.getString("edmPreview"));
            artObject.setImage_url(url);
        } catch(JSONException e){
            e.printStackTrace();
            System.out.println("No URL associated with JSON object");
            artObject.setImage_url("http://orig05.deviantart.net/6651/f/2009/210/7/2/dead_face_by_krasus.jpg");
            }
        try{
            String name = stripQuotesBraces(jsonObject.getString("title"));
            artObject.setName(name);
        } catch(JSONException e){
            e.printStackTrace();
            System.out.println("No name associated with piece");
            artObject.setName("NN");
        }
        try{

            artObject.setAuthor(stripBracesQuotes(jsonObject.getString("dcCreator")));

            //artObject.setImage(image);
            System.out.println("Art for today" + artObject.getName() + " " + artObject.getAuthor());
            //System.out.println("Art for today"+jsonObject.getString("title")+" "+jsonObject.getString("dcCreator"));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("No author name associated with JSON");
            artObject.setAuthor("NN");
        }
        return artObject;
    }

    public ArtObject parseArtObjectFromJSON(JSONObject jsonObject){
        ArtObject artObject = new ArtObject();

        //Gson gson = new Gson();

       // ArtDownloader




        try {
            String url = stripQuotes(jsonObject.getString("previewUrl"));
            artObject.setImage_url(url);
        } catch(JSONException e){
            e.printStackTrace();
            System.out.println("No URL associated with JSON object");
            artObject.setImage_url("http://orig05.deviantart.net/6651/f/2009/210/7/2/dead_face_by_krasus.jpg");
        }
        try{
            String name = stripQuotes(jsonObject.getString("title"));
            artObject.setName(name);
        } catch(JSONException e){
            e.printStackTrace();
            System.out.println("No title associated with piece");
            artObject.setName("NN");
        }
        try{
            artObject.setAuthor(stripQuotes(jsonObject.getString("author")));
            System.out.println("Art for today" + artObject.getName() + " " + artObject.getAuthor());

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("No author name associated with JSON");
            artObject.setAuthor("NN");
        }


        return artObject;
    }




    public Bitmap getBitmapFromDirectURL(String urlAddress){
        Bitmap image = null;
        System.out.println(urlAddress);
        URL url = null;
        try {
            url = new URL(urlAddress);
            image = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (Exception e){
            System.out.println("Image download failed");

            e.printStackTrace();
        }
        return image;
    }

    public String saveImageToStorage(Bitmap bitmapImage){

            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,"today_art.jpg");

            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(mypath);

                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                System.out.println("ImageSaved!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return directory.getAbsolutePath();

    }
    public Bitmap loadImageFromStorage(String path){
        try {
            File f=new File(path, "today_art.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            System.out.println("Image loaded!");
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    /*
    Methods to work on formatting strings read from JSON
     */
    public String stripQuotes(String oldOne){
        String newOne = (String) oldOne.replaceAll("^\"|\"$","");
        return newOne;
    }

    public String stripBackslashAndBraces(String oldOne){

        String newOne = oldOne.replaceAll("\\\\","");

        //System.out.println(newOne);
        String finalOne = (String) newOne.subSequence(2, newOne.length() - 2);
        return finalOne;
    }

    public String stripQuotesBraces(String oldOne){
        return oldOne;//.substring((oldOne.indexOf("\"[")+2),oldOne.indexOf("]\""));
    }
    public String stripBracesQuotes(String oldOne){
        return oldOne.substring((oldOne.indexOf("[\"")+2),oldOne.indexOf("\"]"));
    }

    /**
     * send REST request, parse response, download image and put everything in ArtObject, cache into SharedPreferences

    public void getJSON(String address, Context context){
        final Context ctxt = context;
        final SharedPreferencesController spcLocal = new SharedPreferencesController(ctxt);
        JSONObject artJSON;
        ArtDownloadRestClient.get(address, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //artObject = ad.parseArtObjectFromJSON(response);

                try {
                    String url = response.getString("image_url");
                    Log.i("JSON OBJECT URL", "" + url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //new API
                try {
                    artJSON = response.getJSONObject("dailyart");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //String jsonString = response.toString();
                //artObject = gson.fromJson(jsonString,ArtObject.class);

                String jsonString = artJSON.toString();
                artObject = gson.fromJson(jsonString,ArtObject.class);


                spcLocal.putArt(Tags.ART_CACHE, artObject);
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

                // Do something with the response

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject json) {
                //Toast.makeText(getApplicationContext(), "Unable to download now", Toast.LENGTH_LONG).show();
                //spcLocal = new SharedPreferencesController(ctxt);
                artObject = spcLocal.getArtObject(Tags.ART_CACHE, null);
                //artObject = getFromCache(STORAGE_KEY);
            }
        });

    }**/
}
