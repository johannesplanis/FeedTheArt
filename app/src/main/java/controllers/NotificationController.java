package controllers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.planis.johannes.feedtheart.bambino.R;

import activities.CatActivity;

/**
 * TODO builder?
 * Created by JOHANNES on 8/27/2015.
 */
public class NotificationController {

    /*
    http://android-developers.blogspot.com/2015/08/get-dos-and-donts-for-notifications.html
     */
    static public void issueNotification(Context context, String header, String message,int color){

    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
    long[] pattern = {0,500};

    NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.notificationicon)
            .setLargeIcon(bitmap)
            .setContentTitle(header)
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
