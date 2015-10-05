package geofencing;

import android.util.Log;

import java.util.ArrayList;

import cat.Constants;

/**
 * Created by JOHANNES on 10/5/2015.
 */
public class VenuesDevelopmentMode {

    public static ArrayList<VenueObject> sampleVenues(){
        ArrayList<VenueObject> vo = new ArrayList<>();
        vo.add(new VenueObject(Constants.RETORYKA_ID,1234,5,"www.retoryka.pl","home sweet home","Retoryka",Constants.RETORYKA_RADIUS_METERS,Constants.RETORYKA_LONGITUDE,Constants.RETORYKA_LATITUDE));
        vo.add(new VenueObject(Constants.MUZEUM_NARODOWE_ID,1234,20,"www.mnk.pl","Muzeum Narodowe wielkie jest","Muzeum Narodowe w Krakowie",Constants.MUZEUM_NARODOWE_RADIUS_METERS,Constants.MUZEUM_NARODOWE_LONGITUDE,Constants.MUZEUM_NARODOWE_LATITUDE));
        vo.add(new VenueObject(Constants.MUZEUM_WITRAZU_ID,1234,20,"www.mnk.pl","Muzeum Witrażu na Alejach Trzech Wieszczów","Muzeum Witrażu",Constants.MUZEUM_WITRAZU_RADIUS_METERS,Constants.MUZEUM_WITRAZU_LONGITUDE,Constants.MUZEUM_WITRAZU_LATITUDE));
        for(VenueObject v:vo){
            Log.i("VENUES ON MAP", "" + v.getmId() + " " + v.getmLatitude() + ", " + v.getmLongitude() + ", " + v.getName());

        }



        return vo;
}


}
