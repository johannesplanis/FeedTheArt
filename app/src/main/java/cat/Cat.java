package cat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import backgroundcat.CatNotifications;
import menuactivity.SettingsController;

/**
 * Created by JOHANNES on 9/8/2015.
 */
public class Cat {
    static AtomicInteger nextID = new AtomicInteger();
    private int ID;
    private String name;
    private int character;
    private double foodLevel; //between 0 and 101(101 bc cat's gotta stay full for some time)
    private double lastTimestamp;
    private double coefficient; //how fast it's getting hungry
    private int level;
    private double NUDGE_LEVEL = 100;
    private double ALARM_LEVEL = 25;
    private double CRITICAL_LEVEL = 5;
    private double DEAD = 0f;
    private SettingsController sc;
    public static final String ALARMING_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.cat.Cat";
    public static final String LEVEL_OF_ALARM = "LEVEL_OF_ALARM";
    //when you read cat fields from any storage
    public Cat(double lastTimestamp, int ID,String name, int character, double foodLevel, double coefficient, int level,Context context) {

        refreshSettings(context);
        this.lastTimestamp = lastTimestamp;
        this.ID = ID;
        this.name = name;
        this.character = character;
        this.foodLevel = foodLevel;
        this.coefficient = coefficient;
        this.level = level;
    }

    //both constructors when you create new cat in creator and want to ensure unique ID
    public Cat(String name, int character, double foodLevel, double coefficient, int level,Context context) {

        refreshSettings(context);
        this.lastTimestamp = System.currentTimeMillis();
        this.ID = nextID.incrementAndGet();
        this.name = name;
        this.character = character;
        this.foodLevel = foodLevel;
        this.coefficient = coefficient;
        this.level = level;
    }

    public Cat(Context context){


        this.lastTimestamp = System.currentTimeMillis();
        this.ID = nextID.incrementAndGet();
        this.name = "Noname" ;
        this.character = 1;
        this.foodLevel = 101;
        this.level = 1;
        refreshSettings(context);
    }



    public int getID(){
        return ID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public double getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(double foodLevel) {
        this.foodLevel = foodLevel;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    //

    /**
     * read from settings info about current preferences
     * useful with fast-paced gameplay
     */
    public void refreshSettings(Context context){

        sc = new SettingsController(context);

        if(sc!=null){
            float[] levels = sc.getAlarmLevels();
            float coeff = sc.getStarvingSpeed();
            if(levels.length==3) {
                this.NUDGE_LEVEL = Double.parseDouble(Float.toString(levels[0]));
                this.ALARM_LEVEL = Double.parseDouble(Float.toString(levels[1]));
                this.CRITICAL_LEVEL = Double.parseDouble(Float.toString(levels[2]));

                Log.i("SETTINGS","Food levels loaded!");
            }
            if(coeff>0){
                this.coefficient = coeff;
            } else{
                this.coefficient = 0.0001;
            }
        }   else{
            Log.e("SETTINGS","Controller is null!");
        }

    }

    /**
     * according to last known score and timestamp, calculate current score and return it
     * update values of last timestamp and food level
     * send broadcasts when food level is alarming
     * @param context
     * @return
     */
    public double updateFoodLevel(Context context){
        double timestamp = System.currentTimeMillis();
        double timeElapsed = timestamp-this.lastTimestamp;
        this.lastTimestamp = timestamp;
        double previousFoodLevel = this.foodLevel;
        this.foodLevel = this.foodLevel - this.coefficient*timeElapsed;
        refreshSettings(context);

        Intent broadcastIntent = new Intent(ALARMING_FOODLEVEL_ACTION);

        if(foodLevel<0){
            this.foodLevel=0;
        }

        if (previousFoodLevel>0&&foodLevel<=0){
            broadcastIntent.putExtra(LEVEL_OF_ALARM,DEAD);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM,"DEAD");

        }else if(previousFoodLevel>=NUDGE_LEVEL&&foodLevel<=NUDGE_LEVEL&&foodLevel>=ALARM_LEVEL){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", NUDGE_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM, "NUDGE");
            //color yellow
            if(sc.isNotificationPermission()) {
                CatNotifications.issueNotification(context, "Hurry!", "Cat's getting hungry", Tags.APP_COLOR_NUDGE);
            }

        }else if(previousFoodLevel>=ALARM_LEVEL&&foodLevel<=ALARM_LEVEL&&foodLevel>=CRITICAL_LEVEL){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", ALARM_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM, "ALARM");
            //color orange
            if(sc.isNotificationPermission()) {
                CatNotifications.issueNotification(context, "Hurry!", "Cat's really hungry", Tags.APP_COLOR_ALARM);
            }

        }else if(previousFoodLevel>=CRITICAL_LEVEL&&foodLevel<=CRITICAL_LEVEL&&foodLevel>=DEAD){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", CRITICAL_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM, "CRITICAL");
            //red color
            if(sc.isNotificationPermission()) {
                CatNotifications.issueNotification(context, "Hurry!", "Kitty is starving!", Tags.APP_COLOR_CRITICAL);
            }

        } else if(foodLevel>NUDGE_LEVEL){
            Log.i(LEVEL_OF_ALARM,"NONE");
        }
        return foodLevel;
    }

    //feed the Art!


    /**
     * increase value by the certain factor
     */
    public double feedTheArtByPercent(double percent){


        this.foodLevel +=this.foodLevel*percent/100;
        if(this.foodLevel>101){
            this.foodLevel = 101;
        } else if(this.foodLevel<0){
            this.foodLevel = 0;
        }
        return this.foodLevel;

    }
    /**
     *
     * increment is the value between -101 and 101
     */
    public double feedTheArtByValue(double increment){


        this.foodLevel +=increment;
        if(this.foodLevel>101){
            this.foodLevel = 101;
        } else if (this.foodLevel<0){
            this.foodLevel = 0;
        }
        return this.foodLevel;
    }



}
