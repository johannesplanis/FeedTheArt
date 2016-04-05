package activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.planis.johannes.feedtheart.bambino.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import geofencing.VenueObject;
import geofencing.VenuesDevelopmentMode;
import model.Constants;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {


    @Bind(R.id.cat_map_back_button)
    ImageView backButton;


    private Marker marker;
    private ArrayList<VenueObject> vo;
    private int counter;

    public static final double RADIUS_OF_EARTH_METERS = 6371009;
    private float mStrokeWidth = 4.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        checkPermissions();

        backButton.setOnClickListener(this);
        vo = VenuesDevelopmentMode.sampleVenues();

    }

    private void setupMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(backButton, "Potrzebujemy Twojej lokalizacji do określenia, kiedy Kot jest karmiony.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            setupMap();
        }
    }


    private void initMaps(final GoogleMap map) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            AlertDialog alert = new AlertDialog.Builder(this).setTitle(R.string.location_rationale_title).setCancelable(false).setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    toCat("NOTIFICATION");
                }
            }).setMessage(R.string.location_rationale).show();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
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
                        counter = 1;
                    }
                }
            });

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (VenueObject v : vo) {

                        addPlace(v, map);
                    }
                }
            }, 1000);
        }
    }

    private void addPlace(VenueObject v, GoogleMap map) {
        Log.i("VENUES ON MAP", "" + v.getmId() + " " + v.getmLatitude() + ", " + v.getmLongitude() + ", " + v.getName());
        LatLng latLng = new LatLng(v.getmLatitude(), v.getmLongitude());
        String name = v.getName();
        double radius = v.getmRadius();
        String snippet = v.getDescription();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(name)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .flat(true);
        map.addMarker(markerOptions);
        new GeofenceCircle(latLng, radius, map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initMaps(googleMap);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cat_map_back_button:
                toCat("NOTIFICATION");
                break;
        }
    }

    private class GeofenceCircle {

        private final Circle circle;
        private double radius;

        public GeofenceCircle(LatLng center, double radius, GoogleMap map) {
            this.radius = radius;

            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mStrokeWidth)
                    .strokeColor(ContextCompat.getColor(MapsActivity.this,R.color.button_greenish))
                    .fillColor(ContextCompat.getColor(MapsActivity.this,R.color.grey_cyan)));
        }

        public GeofenceCircle(LatLng center, LatLng radiusLatLng, GoogleMap map) {
            this.radius = toRadiusMeters(center, radiusLatLng);

            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mStrokeWidth)
                    .strokeColor(ContextCompat.getColor(MapsActivity.this,Color.CYAN))
                    .fillColor(ContextCompat.getColor(MapsActivity.this,R.color.grey_cyan)));
        }

    }

    /**
     * Generate LatLng of radius marker
     */
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setupMap();

                }
            }
        }
    }
}
