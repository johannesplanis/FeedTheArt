package model;

/**
 * flags for accessing storage, fragment manipulation
 */
public class Tags {


    //Shared preferences
    public static final String CAT_INSTANCES_COUNT = "CAT_INSTANCES_COUNT";
    public static final String CURRENT_GAME_INSTANCE = "CURRENT_GAME_INSTANCE";

    public static final String CAT = "CAT";
    public static final String CHARACTER = "CHARACTER";
    public static final String NAME = "NAME";
    public static final String LEVEL_OF_ALARM = "LEVEL_OF_ALARM";

    public static final String SETTINGS = "SETTINGS";

    public static final String GEOFENCE = "GEOFENCE";
    /**
     * actions
     */
    public static final String ALARMING_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.cat.Cat";

    //used to update foodlevel when computed by service
    public static final String UPDATE_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.update.foodlevel";


    public static final String SCORE_INCREMENT = "SCORE_INCREMENT";
    public static final String SCORE_INCREMENT_FOREGROUND = "SCORE_INCREMENT_FOREGROUND"; //introduced to indicate flow of info
    public static final String SCORE_INCREMENT_FIELD = "SCORE_INCREMENT_FIELD";

    //art cache

    public static final String REQ_ID = "REQ_ID";
}
