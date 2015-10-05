package geofencing;

import android.content.Context;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.HashSet;

import cat.Tags;
import controllers.SharedPreferencesController;

/**
 * Created by JOHANNES on 10/5/2015.
 */
public class VenueStorageController {


    public VenueStorageController() {
    }

    public ArrayList<String> getVenuesIds(Context context){
        ArrayList<String> geofencesList;
        SharedPreferencesController sp = new SharedPreferencesController(context);
        geofencesList = new ArrayList<>(sp.getStringSet("ids_"+Tags.GEOFENCE,new HashSet<String>()));

        return geofencesList;
    }

    public void putIds(Context context, ArrayList<String> ids){

        for(String s:ids){
            putIdIntoIds(context,s);
        }
    }

    public void putIdIntoIds(Context context, String mId){
        SharedPreferencesController sp = new SharedPreferencesController(context);
        ArrayList<String> geofencesList = new ArrayList<>(sp.getStringSet("ids_"+Tags.GEOFENCE,new HashSet<String>()));
        geofencesList.add(mId);

        sp.putStringSet("ids_" + Tags.GEOFENCE, new HashSet<String>(geofencesList));
    }




    /**
     * iterate over array of ids and retrieve current venues list
     * @param context
     * @return
     */
    public ArrayList<VenueObject> getVenues(Context context){
        SharedPreferencesController sp = new SharedPreferencesController(context);
        ArrayList<String> ids = getVenuesIds(context);
        ArrayList<VenueObject> venues = new ArrayList<>();
        VenueObject temp;
        for(String id:ids){
            temp = sp.getVenueObject(Tags.GEOFENCE+id,null);
            if(temp!=null){
                venues.add(temp);
            }
        }
        return venues;
    }

    /**
     *  puts venues list into sp, puts ids into ids
     * @param context
     * @param venues
     * @return returns current list of ids stored in SharedPreferences
     */
    public ArrayList<String> putVenues(Context context, ArrayList<VenueObject> venues){
        SharedPreferencesController spc = new SharedPreferencesController(context);
        for(VenueObject v:venues){
            spc.putVenue(v.getmId(),v);
            putIdIntoIds(context,v.getmId());
        }
        return getVenuesIds(context);
    }

    /**
     * put single venue, update id list
     * @param context
     * @param venue
     * @return id of venue put into sp
     */
    public String putVenue(Context context, VenueObject venue){
        SharedPreferencesController spc = new SharedPreferencesController(context);
        spc.putVenue(Tags.GEOFENCE+ venue.getmId(),venue);
        putIdIntoIds(context,venue.getmId());
        return  venue.getmId();
    }

    public VenueObject getVenue(Context context,String id){
        SharedPreferencesController spc = new SharedPreferencesController(context);
        return spc.getVenueObject(Tags.GEOFENCE + id, new VenueObject());
    }

    public void removeVenueAndItsId(Context context, String id){

        ArrayList<VenueObject> venues = getVenues(context);
        ArrayList<String> ids = getVenuesIds(context);
        venues.remove(getVenue(context,id));
        ids.remove(id);
        putVenues(context, venues);
        putIds(context,ids);
    }

    /**
     * gets all geofences from storage in a form of arraylist by getting venue objects and extracting geofencing fields
     * @param context
     * @return
     */
    public ArrayList<Geofence> getGeofencesFromStorage(Context context){
        ArrayList<VenueObject> venues = getVenues(context);
        ArrayList<Geofence> geofences = new ArrayList<>();
        VenueGeofence vTemp;
        for(VenueObject v:venues){
            vTemp = new VenueGeofence(v.mId,v.mLatitude,v.mLongitude,v.mRadius,v.getmExpirationDuration(),v.getmTransitionType());
            geofences.add(vTemp.toGeofence());
        }
        return geofences;
    }


}
