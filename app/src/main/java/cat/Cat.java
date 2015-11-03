package cat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import controllers.NotificationController;
import controllers.SettingsController;

/**
 * PROTIP: If you need to serialize object later, make sure fields in it are only primitives, otherwise it will leak, gc will restart frequently, stack will overflow
 * Created by JOHANNES on 9/8/2015.
 */
 public class Cat {
    static AtomicInteger nextID = new AtomicInteger();
    private int ID;
    private String name;
    private int character;
    private double foodLevel; //between 0 and 101(101 bc cat's gotta stay full for some time)
    private double lastTimestamp;
    private static final double defaultCoefficient = 0.0001d;
    private double coefficient; //how fast it's getting hungry
    private int level;
    public static final double NUDGE_LEVEL = 100d;
    public static final double ALARM_LEVEL = 95d;
    public static final double CRITICAL_LEVEL = 5d;

    public int getHumour() {
        return humour;
    }

    public void setHumour(int humour) {
        this.humour = humour;
    }

    private double DEAD = 0d;
    private int humour = 0;

    //when you read cat fields from any storage
    public Cat(double lastTimestamp, int ID,String name, int character, double foodLevel, double coefficient, int level,Context context) {

        //refreshSettings(context);
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

        //refreshSettings(context);
        this.lastTimestamp = System.currentTimeMillis();
        this.ID = nextID.incrementAndGet();
        this.name = name;
        this.character = character;
        this.foodLevel = foodLevel;
        this.coefficient = coefficient;
        this.level = level;
    }

    public Cat(){

        this.lastTimestamp = System.currentTimeMillis();
        this.ID = nextID.incrementAndGet();
        this.name = "Noname" ;
        this.character = 1;
        this.foodLevel = 101;
        this.level = 1;
        this.coefficient = 1;//0.0001;
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
        SettingsController sc = new SettingsController(context);
        double coe = sc.getStarvingSpeed();
        Log.i("CREATOR PUT",""+coe);
            if(coe>0){
                this.coefficient = coe;
            } else{
                this.coefficient = 0.0001;
            }
    }

    /**
     * according to last known score and timestamp, calculate current score and return it
     * update values of last timestamp and food level
     * send broadcasts when food level is alarming
     * @param context
     * @return
     */
    public double updateFoodLevel(Context context, double coeff){
        SettingsController sc = new SettingsController(context);
        double timestamp = System.currentTimeMillis();
        double timeElapsed = timestamp-this.lastTimestamp;
        this.lastTimestamp = timestamp;
        double previousFoodLevel = this.foodLevel;
        this.foodLevel = this.foodLevel - 2*coeff*this.coefficient*timeElapsed;//2 bcin settings it will be as 0-100 and default will be 50

        Intent broadcastIntent = new Intent(Tags.ALARMING_FOODLEVEL_ACTION);
        Intent humourBroadcastIntent = new Intent("HUMOUR");
        if(foodLevel<0){
            this.foodLevel=0;
        }

        if (previousFoodLevel>0&&foodLevel<=0){
            broadcastIntent.putExtra(Tags.LEVEL_OF_ALARM,DEAD);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(Tags.LEVEL_OF_ALARM,"DEAD");

        }else if(previousFoodLevel>=NUDGE_LEVEL&&foodLevel<=NUDGE_LEVEL&&foodLevel>=ALARM_LEVEL){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", NUDGE_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(Tags.LEVEL_OF_ALARM, "NUDGE");
            //color yellow
            if(sc.isNotificationPermission()) {
                NotificationController.issueNotification(context, "Hurry!", "Cat's getting hungry", Constants.APP_COLOR_NUDGE);
            }
            humourBroadcastIntent.putExtra("HUMOUR","NUDGE");
            LocalBroadcastManager.getInstance(context).sendBroadcast(humourBroadcastIntent);


        }else if(previousFoodLevel>=ALARM_LEVEL&&foodLevel<=ALARM_LEVEL&&foodLevel>=CRITICAL_LEVEL){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", ALARM_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(Tags.LEVEL_OF_ALARM, "ALARM");
            //color orange
            if(sc.isNotificationPermission()) {
                NotificationController.issueNotification(context, "Hurry!", "Cat's really hungry", Constants.APP_COLOR_ALARM);
            }

            humourBroadcastIntent.putExtra("HUMOUR","HUNGRING");
            LocalBroadcastManager.getInstance(context).sendBroadcast(humourBroadcastIntent);

        }else if(previousFoodLevel>=CRITICAL_LEVEL&&foodLevel<=CRITICAL_LEVEL&&foodLevel>=DEAD){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", CRITICAL_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(Tags.LEVEL_OF_ALARM, "CRITICAL");
            //red color
            if(sc.isNotificationPermission()) {
                NotificationController.issueNotification(context, "Hurry!", "Kitty is starving!", Constants.APP_COLOR_CRITICAL);
            }
            humourBroadcastIntent.putExtra("HUMOUR","STARVING");
            LocalBroadcastManager.getInstance(context).sendBroadcast(humourBroadcastIntent);

        } else if(foodLevel>NUDGE_LEVEL){
            Log.i(Tags.LEVEL_OF_ALARM,"NONE");
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
     * increment food value without checking levels, it will be updated soon in foreground
     * T ODO make sure when you reach level in background with this method, it will notify immediately, not wait for next iteration of alarm
     * increment is the value between -101 and 101
     */
    public double feedTheArtByValue(Context ctxt, double increment){
        double previousFoodLevel = this.foodLevel;
        Intent humourBroadcastIntent = new Intent("HUMOUR");


        this.foodLevel +=increment;
        Log.i("FOOD_LEVEL_BY_VALUE",""+this.foodLevel);
        if(this.foodLevel>101){
            this.foodLevel = 101;
        } else if (this.foodLevel<0){
            this.foodLevel = 0;
        }

        Intent broadcastIntent = new Intent(Tags.UPDATE_FOODLEVEL_ACTION);
        double message = this.foodLevel;
        Log.i("FOOD_LEVEL_BY_VALUE1",""+message);
        broadcastIntent.putExtra("SERVICE_BROADCAST", message);

        LocalBroadcastManager.getInstance(ctxt).sendBroadcast(broadcastIntent);



        if(previousFoodLevel<=ALARM_LEVEL&&foodLevel>=ALARM_LEVEL){
            Log.i("HUMOUR","SERVICE FEEDING");
            humourBroadcastIntent.putExtra("HUMOUR","FEEDING");
            LocalBroadcastManager.getInstance(ctxt).sendBroadcast(humourBroadcastIntent);
        } else if(previousFoodLevel<=CRITICAL_LEVEL&&foodLevel>CRITICAL_LEVEL){
            Log.i("HUMOUR","SERVICE FEEDING FROM STARVE");
            humourBroadcastIntent.putExtra("HUMOUR","FEEDING_FROM_STARVE");
            LocalBroadcastManager.getInstance(ctxt).sendBroadcast(humourBroadcastIntent);
        }
        return this.foodLevel;
    }


    /**
     * copy cat to new variable, but assign it current context. Works with 'transient' keyword
     * @param context
     * @return
     */
    public Cat copyCat(Context context){
        Cat cat = this;
        SettingsController sc = new SettingsController(context);
        sc.refreshSettings(cat);
        return cat;
    }

}
