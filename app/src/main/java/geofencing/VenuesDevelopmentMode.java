package geofencing;

import android.util.Log;

import com.google.android.gms.location.Geofence;

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
        vo.add(new VenueObject(Constants.MUZEUM_WITRAZU_ID,1234,20,"www.mnk.pl","Daj kamienia!","Muzeum Witrażu",Constants.MUZEUM_WITRAZU_RADIUS_METERS,Constants.MUZEUM_WITRAZU_LONGITUDE,Constants.MUZEUM_WITRAZU_LATITUDE));
        vo.add(new VenueObject(Constants.MICROSCUP_ID,1234,5,"","Microscup - księgarnio-kawiarnia przy ulicy","microscup",Constants.MICROSCUP_RADIUS_METERS, Constants.MICROSCUP_LONGITUDE, Constants.MICROSCUP_LATITUDE));
        vo.add(new VenueObject(Constants.PN_ID,1234,60,"http://alchemia.com.pl/","Zapiekankowy Plac Nowy","Plac Nowy",Constants.PN_RADIUS_METERS,Constants.PN_LONGITUDE,Constants.PN_LATITUDE));
        vo.add(new VenueObject(Constants.MAKOWA_ID,1234,60,"","Makowa w samym centrum Krakowa","Makowa",Constants.MAKOWA_RADIUS_METERS,Constants.MAKOWA_LONGITUDE,Constants.MAKOWA_LATITUDE));
        vo.add(new VenueObject(Constants.AUDITORIUM_MAXIMUM_ID,1234,60,"http://uj.edu.pl","Representative venue of Jagiellonian University","Auditorium Maximum",Constants.AUDITORIUM_MAXIMUM_RADIUS_METERS,Constants.AUDITORIUM_MAXIMUM_LONGITUDE,Constants.AUDITORIUM_MAXIMUM_LATITUDE));
        vo.add(new VenueObject(Constants.SZCZYRK_ID,1234,60,"","Szczyrk gdzieś w górach.","Szczyrk",Constants.SZCZYRK_RADIUS_METERS,Constants.SZCZYRK_LONGITUDE,Constants.SZCZYRK_LATITUDE));
        vo.add(new VenueObject(Constants.CREATIVESTYLE_ID,1234,60,"","Open Culture Hack.","creativestyle och",Constants.CREATIVESTYLE_RADIUS_METERS,Constants.CREATIVESTYLE_LONGITUDE,Constants.CREATIVESTYLE_LATITUDE));
        vo.add(new VenueObject(Constants.MOCAK_ID,1234,60,"","Muzeum sztuki współczesnej w Krakowie","MOCAK",Constants.MOCAK_RADIUS_METERS,Constants.MOCAK_LONGITUDE,Constants.MOCAK_LATITUDE));


       for(VenueObject v:vo){
            Log.i("VENUES ON MAP", "" + v.getmId() + " " + v.getmLatitude() + ", " + v.getmLongitude() + ", " + v.getName());

        }



        return vo;
}
    public static ArrayList<VenueGeofence> sampleVenueGeofences(int timeout){
        ArrayList<VenueObject> vo = sampleVenues();
        ArrayList<VenueGeofence> vg = new ArrayList<>();

        for(VenueObject v:vo){
            vg.add(v.getVenueGeofenceFromVenue(timeout, Geofence.GEOFENCE_TRANSITION_DWELL));
        }
        return vg;
    }

    public static ArrayList<VenueGeofence> sampleVenueGeofences(long timeout){
        ArrayList<VenueObject> vo = sampleVenues();
        ArrayList<VenueGeofence> vg = new ArrayList<>();

        for(VenueObject v:vo){
            vg.add(v.getVenueGeofenceFromVenue(timeout, Geofence.GEOFENCE_TRANSITION_DWELL));
        }
        return vg;
    }


    public static ArrayList<Geofence> sampleGeofences(int timeout){
        ArrayList<VenueObject> vo = sampleVenues();
        ArrayList<Geofence> ga = new ArrayList<>();

        for(VenueObject v:vo){
            ga.add(v.getVenueGeofenceFromVenue(timeout, Geofence.GEOFENCE_TRANSITION_DWELL).toGeofence());
        }
        return ga;
    }


    public static ArrayList<Geofence> sampleGeofences(long timeout){
        ArrayList<VenueObject> vo = sampleVenues();
        ArrayList<Geofence> ga = new ArrayList<>();

        for(VenueObject v:vo){
            ga.add(v.getVenueGeofenceFromVenue(timeout, Geofence.GEOFENCE_TRANSITION_DWELL).toGeofence());
        }
        return ga;
    }
}
