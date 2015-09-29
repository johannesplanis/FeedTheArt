package cat;

/**
 * flags for accessing storage, fragment manipulation
 */
public class Tags {

    //Fragment tags
    public static final String MENU_FRAGMENT = "MEF";
    public static final String SPLASH_FRAGMENT = "SPF";
    public static final String SETTINGS_FRAGMENT = "MSF";
    public static final String TUTORIAL_FRAGMENT = "TUF";
    public static final String NEWCAT_CHOOSE_FRAGMENT = "NCF";
    public static final String NEWCAT_NAME_FRAGMENT = "NNF";


    //Shared preferences
    public static final String CAT_INSTANCES = "CAT_INSTANCES";
    public static final String CAT_INSTANCES_COUNT = "CAT_INSTANCES_COUNT";
    public static final String CURRENT_GAME_INFO = "CURRENT_GAME_INFO";
    public static final String CURRENT_GAME_INSTANCE = "CURRENT_GAME_INSTANCE";

    public static final String CAT = "CAT";
    public static final String CHARACTER = "CHARACTER";
    public static final String NAME = "NAME";
    public static final String LEVEL_OF_ALARM = "LEVEL_OF_ALARM";

    public static final int APP_COLOR_SUCCESS = 0xFF40D211;

    public static final int APP_COLOR_NUDGE = 0xFFE8ED00;
    public static final int APP_COLOR_ALARM = 0xFFED7800;
    public static final int APP_COLOR_CRITICAL = 0xFFED1000;


    public static final String SETTINGS = "SETTINGS";

    public static final int INTERVAL_FOREGROUND = 500;
    public static final int INTERVAL_BACKGROUND = 1000*10;

    public static final int INTERVAL_GEOFENCE = 59*1000;

    /**
     * actions
     */
    public static final String ALARMING_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.cat.Cat";

    //used to update foodlevel when computed by service
    public static final String UPDATE_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.update.foodlevel";

    //used to update foodlevel when incremented by geofence or art etc
    public static final String SCORE_UPDATE_FOODLEVEL_ACTION = "SCORE_UPDATE_FOODLEVEL";

    public static final String SCORE_INCREMENT = "SCORE_INCREMENT";
    public static final String SCORE_INCREMENT_FOREGROUND = "SCORE_INCREMENT_FOREGROUND"; //introduced to indicate flow of info
    public static final String SCORE_INCREMENT_FIELD = "SCORE_INCREMENT_FIELD";


}
