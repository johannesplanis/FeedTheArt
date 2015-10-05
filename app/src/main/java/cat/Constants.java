package cat;

import android.net.Uri;

import com.google.android.gms.location.Geofence;
import com.planis.johannes.catprototype.R;

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

    // For the purposes of this demo, the geofences are hard-coded and should not expire.
    // An app with dynamically-created geofences would want to include a reasonable expiration time.
    public static final long GEOFENCE_EXPIRATION_TIME = Geofence.NEVER_EXPIRE;

    // Geofence parameters for the Android building on Google's main campus in Mountain View.
    public static final String ANDROID_BUILDING_ID = "1";
    public static final double ANDROID_BUILDING_LATITUDE = 37.420092;
    public static final double ANDROID_BUILDING_LONGITUDE = -122.083648;
    public static final float ANDROID_BUILDING_RADIUS_METERS = 60.0f;

    // Geofence parameters for the Yerba Buena Gardens near the Moscone Center in San Francisco.
    public static final String YERBA_BUENA_ID = "2";
    public static final double YERBA_BUENA_LATITUDE = 37.784886;
    public static final double YERBA_BUENA_LONGITUDE = -122.402671;
    public static final float YERBA_BUENA_RADIUS_METERS = 100.0f;

    //Geofence parameters for Retoryka
    public static final String RETORYKA_ID = "1";
    public static final double RETORYKA_LATITUDE = 50.0574927 ;
    public static final double RETORYKA_LONGITUDE = 19.9284563;
    public static final float RETORYKA_RADIUS_METERS = 100.0f;


    //Geofence parameters for Muzeum Narodowe
    public static final String MUZEUM_NARODOWE_ID = "2";
    public static final double MUZEUM_NARODOWE_LATITUDE = 50.0595709;
    public static final double MUZEUM_NARODOWE_LONGITUDE = 19.9252636;
    public static final float MUZEUM_NARODOWE_RADIUS_METERS = 100.0f;

    //Geofence parameters for Muzeum Witrażu
    public static final String MUZEUM_WITRAZU_ID = "3";
    public static final double MUZEUM_WITRAZU_LATITUDE = 50.0585207;
    public static final double MUZEUM_WITRAZU_LONGITUDE = 19.9222739;
    public static final float MUZEUM_WITRAZU_RADIUS_METERS = 100.0f;


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
    public static final String PICASSO_CAT = "Picasso cat";
    public static final String BASIC_CAT = "Evil cat";
    public static final String PIRAT_CAT = "Pirate cat";
    public static final String[] catsNames = new String[]{PICASSO_CAT,BASIC_CAT,PIRAT_CAT};
    public final static Integer[] catImageResIds = new Integer[]{R.drawable.kitty_picasso,R.drawable.kitty_standard,R.drawable.kitty_pirate};


    //cat art constants
    public static final String baseApiUrl = "http://192.168.2.105:8080";
    public static final String relativeApiUrl = "/art.json";

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


    private Constants() {
    }
}
