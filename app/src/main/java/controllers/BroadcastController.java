package controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * controller for sending broadcasts
 * TODO: arrays of messages at once, serialize, send
 * Created by JOHANNES on 9/11/2015.
 */
public class BroadcastController {

    static public void send(Context context, String intentFilter, String tag, String message){
        Intent broadcastIntent = new Intent(intentFilter);
        broadcastIntent.putExtra(tag,message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }


    static public void send(Context context, String intentFilter, String tag, double message){
        Intent broadcastIntent = new Intent(intentFilter);
        broadcastIntent.putExtra(tag,message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
