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
    ArtDownloader(Context context){
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
            artObject.setUrl(url);
        } catch(JSONException e){
            e.printStackTrace();
            System.out.println("No URL associated with JSON object");
            artObject.setUrl("http://orig05.deviantart.net/6651/f/2009/210/7/2/dead_face_by_krasus.jpg");
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

    public String stripBackslashAndBraces(String oldOne){

        String newOne = oldOne.replaceAll("\\\\","");

        //System.out.println(newOne);
        String finalOne = (String) newOne.subSequence(2,newOne.length()-2);
        return finalOne;
    }

    public String stripQuotesBraces(String oldOne){
        return oldOne;//.substring((oldOne.indexOf("\"[")+2),oldOne.indexOf("]\""));
    }
    public String stripBracesQuotes(String oldOne){
        return oldOne.substring((oldOne.indexOf("[\"")+2),oldOne.indexOf("\"]"));
    }
}
