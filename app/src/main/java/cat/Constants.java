package cat;

import android.net.Uri;

import com.planis.johannes.feedtheart.bambino.R;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class Constants {

    public static final int INTERVAL_FOREGROUND = 500;
    public static final int INTERVAL_BACKGROUND = 1000*10;
    public static final int INTERVAL_GEOFENCE = 59*1000;

    public static final String APP_TAG = "FeedTheArt";

    // Request code to attempt to resolve Google Play services connection failures.
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Timeout for making a connection to GoogleApiClient (in milliseconds).
    public static final long CONNECTION_TIME_OUT_MS = 100;



    //Geofence parameters for Retoryka
    public static final String RETORYKA_ID = "1";
    public static final double RETORYKA_LATITUDE = 50.0574927 ;
    public static final double RETORYKA_LONGITUDE = 19.9284563;
    public static final float RETORYKA_RADIUS_METERS = 100.0f;


    //Geofence parameters for Muzeum Narodowe
    public static final String MUZEUM_NARODOWE_ID = "2";
    public static final double MUZEUM_NARODOWE_LATITUDE = 50.0604404;
    public static final double MUZEUM_NARODOWE_LONGITUDE = 19.9237764;
    public static final float MUZEUM_NARODOWE_RADIUS_METERS = 100.0f;

    //Geofence parameters for Muzeum Witrażu
    public static final String MUZEUM_WITRAZU_ID = "3";
    public static final double MUZEUM_WITRAZU_LATITUDE = 50.059008;
    public static final double MUZEUM_WITRAZU_LONGITUDE = 19.925702;
    public static final float MUZEUM_WITRAZU_RADIUS_METERS = 100.0f;

    //Geofence parameters for Microscup
    public static final String MICROSCUP_ID = "4";
    public static final double MICROSCUP_LATITUDE = 50.067327;
    public static final double MICROSCUP_LONGITUDE = 19.918632;
    public static final float MICROSCUP_RADIUS_METERS = 100.0f;

    //Geofence parameters for Makowa
    public static final String MAKOWA_ID = "5";
    public static final double MAKOWA_LATITUDE = 50.027048;
    public static final double MAKOWA_LONGITUDE = 19.966601;
    public static final float MAKOWA_RADIUS_METERS = 100.0f;

    //Geofence parameters for Plac Nowy
    public static final String PN_ID = "6";
    public static final double PN_LATITUDE = 50.052039;
    public static final double PN_LONGITUDE = 19.945077;
    public static final float PN_RADIUS_METERS = 100.0f;

    //Geofence parameters for Auditorium Maximum
    public static final String AUDITORIUM_MAXIMUM_ID = "7";
    public static final double AUDITORIUM_MAXIMUM_LATITUDE = 50.062783;
    public static final double AUDITORIUM_MAXIMUM_LONGITUDE = 19.925192;
    public static final float AUDITORIUM_MAXIMUM_RADIUS_METERS = 70.0f;

    //Geofence parameters for Szczyrk
    public static final String SZCZYRK_ID = "10";
    public static final double SZCZYRK_LATITUDE = 49.714971;
    public static final double SZCZYRK_LONGITUDE = 19.023311;
    public static final float SZCZYRK_RADIUS_METERS = 7000.0f;


    //Geofence parameters for creativestyle
    public static final String CREATIVESTYLE_ID = "9";
    public static final double CREATIVESTYLE_LATITUDE = 50.049807;
    public static final double CREATIVESTYLE_LONGITUDE = 19.961304;
    public static final float CREATIVESTYLE_RADIUS_METERS = 500.0f;

    //Geofence parameters for MOCAK
    public static final String MOCAK_ID = "8";
    public static final double MOCAK_LATITUDE = 50.047308;
    public static final double MOCAK_LONGITUDE = 19.960534;
    public static final float MOCAK_RADIUS_METERS = 70.0f;
    //


    // The constants below are less interesting than those above.

    // Path for the DataItem containing the last geofence id entered.
    public static final String GEOFENCE_DATA_ITEM_PATH = "/geofenceid";
    public static final Uri GEOFENCE_DATA_ITEM_URI =
            new Uri.Builder().scheme("wear").path(GEOFENCE_DATA_ITEM_PATH).build();
    public static final String KEY_GEOFENCE_ID = "geofence_id";

    // Keys for flattened geofences stored in SharedPreferences.
    public static final String KEY_LATITUDE = "com.planis.johannes.catprototype.geofencing.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.planis.johannes.catprototype.geofencing.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.planis.johannes.catprototype.geofencing.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION =
            "com.planis.johannes.catprototype.geofencing.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE =
            "com.planis.johannes.catprototype.geofencing.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys.
    public static final String KEY_PREFIX = "com.planis.johannes.catprototype.geofencing.KEY";

    // Invalid values, used to test geofence storage when retrieving geofences.
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

    @Deprecated
    //placeholder cats
    public static final String SWEET_CAT = "Sweet cat";
    public static final String EVIL_CAT = "Evil cat";
    public static final String PIRATE_CAT = "Pirate cat";
    //public final static String[] imageNames = new String[]{SWEET_CAT, EVIL_CAT, PIRATE_CAT};
    //public final static Integer[] imageResIds = new Integer[]{R.drawable.kitty_sweet,R.drawable.kitty_evil,R.drawable.kitty_pirate};

    //proper cats
    public static final String ARTIST_CAT_CAT = "Artist cat";
    public static final String BASIC_CAT = "Everyday cat";
    public static final String PIRAT_CAT = "Pirate cat";
    public static final String[] catsNames = new String[]{ARTIST_CAT_CAT,BASIC_CAT,PIRAT_CAT};
    public final static Integer[] catImageResIds0 = new Integer[]{R.drawable.cat_painter,R.drawable.cat_basic,R.drawable.cat_pirat}; //images to be included in creator
    public final static Integer[] catImageResIds = new Integer[]{R.drawable.cat_painter,R.drawable.cat_basic,R.drawable.cat_pirat,R.drawable.cat_painter_sad,R.drawable.cat_basic_sad,R.drawable.cat_pirat_sad}; //images to be available to use during game, different humours




    //cat art constants
    /*  old API
    public static final String baseApiUrl = "http://192.168.2.109:8080";
    public static final String relativeApiUrl = "/art.json";
    */
    //New API constants
    public static final String baseApiUrl = "http://feedtheart.us.kkwm.pl/api";
    public static final String relativeApiUrl = "/fta.php";

    //notification colors
    public static final int APP_COLOR_SUCCESS = 0xFF40D211;
    public static final int APP_COLOR_NUDGE = 0xFFE8ED00;
    public static final int APP_COLOR_ALARM = 0xFFED7800;
    public static final int APP_COLOR_CRITICAL = 0xFFED1000;


    //
    public static final String TUTORIAL = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Suspendisse nec ex eu lacus pulvinar sollicitudin. Fusce eu bibendum nibh, non vestibulum orci. " +
            "Etiam gravida purus ex, eget convallis lacus consectetur et. Integer gravida nibh eu ipsum elementum porta. " +
            "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. " +
            "Vestibulum suscipit facilisis ex, id bibendum sem sodales id. Vivamus mauris tellus, feugiat ac felis vel, fringilla efficitur quam. " +
            "Ut consequat erat in condimentum vehicula. Nam suscipit leo ut turpis porttitor, auctor mollis turpis porta. " +
            "Pellentesque leo odio, cursus sed nibh a, dictum mattis dui.";

    //fonts
    public static final String AUSTIE_BOST_KITTEN_KLUB_FONT = "fonts/AustieBostKittenKlub.ttf";

    public static final String NUDGE_REACTION = "Human, I'm getting hungry!";
    public static final String STARVING_REACTION = "Human, I'm starving!";
    public static final String FEEDING_REACTION = "I'm so happy!";
    //Fragments management

    private Constants() {
    }
}
