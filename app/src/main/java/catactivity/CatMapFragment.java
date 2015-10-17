package catactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.planis.johannes.feedtheart.bambino.R;

import java.util.ArrayList;

import geofencing.VenueObject;
import geofencing.VenuesDevelopmentMode;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class CatMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private Marker marker;
    //private String markerInfo;
    private FragmentActivity myContext;
    private ArrayAdapter<Marker> places;
    private ArrayList<VenueObject> vo;
    private int counter;
    private static View view;

    private static final double DEFAULT_RADIUS = 1000000;
    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    private int mStrokeColor = Color.CYAN;
    private int mFillColor = Color.parseColor("#30E0FFFF"); //grey-cyan with 30% opacity
    private float mStrokeWidth = 4.0f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        counter = 0;
        view = inflater.inflate(R.layout.cat_map_fragment,
                container, false);
        initMaps();
        /*if(view!=null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }

        try{

        } catch(InflateException e){

        }
        */

        Button menuButton = (Button) view.findViewById(R.id.cat_map_back_button);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCat();
            }
        });

        vo = VenuesDevelopmentMode.sampleVenues();
        //for tests of async loading of pins



        return view;

    }

    private void initMaps() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            AlertDialog alert = new AlertDialog.Builder(getActivity()).setTitle("Nie spełniasz wymagań aplikacji!").setCancelable(false).setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    toCat();
                }
            }).setMessage("Ta część aplikacji wymaga dostępu do Twojej aktualnej lokalizacji. Sprawdź, czy masz włączoną funkcje lokalizacja w ustawieniach systemowych. Po tym wróć i kontynuuj wykonywanie tej operacji.").show();
        } else {

            if (map == null) {

                // Try to obtain the map from the SupportMapFragment.

                map = ((SupportMapFragment) myContext.getSupportFragmentManager().findFragmentById(R.id.google_maps_fragment)).getMap();



                /*


                SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
                map = mMapFragment.getMap();
                android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.cat_map_container,mMapFragment).commit();
*/
                // Check if we were successful in obtaining the map.
                if (map != null) {
                    //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                    map.setMyLocationEnabled(true);
                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            Log.d("upd", "updatelocation");
                            //map.clear();
                            if (marker != null) {
                                marker.remove();
                            }
                            if (counter == 0) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                MarkerOptions mOption = new MarkerOptions().position(latLng).title("You are here").flat(true);//.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                                marker = map.addMarker(mOption);
                                counter=1;
                            }
                            //markerInfo = marker.getPosition().toString();
                        }
                    });

                    final android.os.Handler handler = new android.os.Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (VenueObject v : vo) {

                                addPlace(v);
                            }
                        }
                    }, 1000);
                }
            }
        }
    }

    private void addPlace(VenueObject v) {
        Log.i("VENUES ON MAP", "" + v.getmId() + " " + v.getmLatitude() + ", " + v.getmLongitude() + ", " + v.getName());
        LatLng latLng = new LatLng(v.getmLatitude(),v.getmLongitude());
        String name = v.getName();
        double radius = v.getmRadius();
        String snippet = v.getDescription();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(name)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .flat(true);
        Marker eMarker = map.addMarker(markerOptions);
        new GeofenceCircle(latLng,radius);
    }

    public void toCat() {
        Activity act = getActivity();
        if (act instanceof CatActivity)
            ((CatActivity) act).toCat();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private class GeofenceCircle {

        private final Circle circle;
        private double radius;
        public GeofenceCircle(LatLng center, double radius) {
            this.radius = radius;

            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mStrokeWidth)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public GeofenceCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);

            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mStrokeWidth)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }

    }

    /** Generate LatLng of radius marker */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }
}
