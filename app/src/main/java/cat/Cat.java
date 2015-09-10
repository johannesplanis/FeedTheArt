package cat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.planis.johannes.catprototype.R;

import java.util.concurrent.atomic.AtomicInteger;

import catactivity.CatActivity;

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

    public static final double NUDGE_LEVEL = 100;
    public static final double ALARM_LEVEL = 25;
    public static final double CRITICAL_LEVEL = 5;
    public static final double DEAD = 0.0;

    public static final String ALARMING_FOODLEVEL_ACTION = "com.planis.johannes.catprototype.cat.Cat";
    public static final String LEVEL_OF_ALARM = "LEVEL_OF_ALARM";
    //when you read cat fields from any storage
    public Cat(double lastTimestamp, int ID,String name, int character, double foodLevel, double coefficient, int level) {
        this.lastTimestamp = lastTimestamp;
        this.ID = ID;
        this.name = name;
        this.character = character;
        this.foodLevel = foodLevel;
        this.coefficient = coefficient;
        this.level = level;
    }

    //both constructors when you create new cat in creator and want to ensure unique ID
    public Cat(String name, int character, double foodLevel, double coefficient, int level) {
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
        this.coefficient = 0.0001;
        this.level = 1;
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
        int color = 0xFF919185;

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
            color = 0xFFE8ED00;
            issueNotification(context ,"Cat's getting hungry",color);


        }else if(previousFoodLevel>=ALARM_LEVEL&&foodLevel<=ALARM_LEVEL&&foodLevel>=CRITICAL_LEVEL){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", ALARM_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM, "ALARM");
            //color orange
            color = 0xFFED7800;
            issueNotification(context, "Cat's really hungry",color);

        }else if(previousFoodLevel>=CRITICAL_LEVEL&&foodLevel<=CRITICAL_LEVEL&&foodLevel>=DEAD){
            broadcastIntent.putExtra("LEVEL_OF_ALARM", CRITICAL_LEVEL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            Log.i(LEVEL_OF_ALARM, "CRITICAL");
            //red color
            color = 0xFFED1000;
            issueNotification(context ,"Kitty is starving!",color);

        } else if(foodLevel>NUDGE_LEVEL){
            Log.i(LEVEL_OF_ALARM,"NONE");
        }
        return foodLevel;
    }

    //feed the Art!


    /**
     *
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

    /**
     * handy method to issue notification in multiple places
     * @param context
     * @param message
     */
    private void issueNotification(Context context, String message,int color){

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.hipsterska_burgernia_dla_kotow_transparent);
        long[] pattern = {0,500};

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notificationicon)
                .setLargeIcon(bitmap)
                .setContentTitle("Hurry")
                .setContentText(message)
                .setColor(color)
                .setVibrate(pattern);
                //.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        Intent intent = new Intent(context, CatActivity.class);
        intent.putExtra("START_MODE","NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(pendingIntent);
        int nNotificationID = 1111;
        NotificationManager nNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nNotificationManager.notify(nNotificationID,nBuilder.build());
    }

}
