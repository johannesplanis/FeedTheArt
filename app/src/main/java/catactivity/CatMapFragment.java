package catactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.planis.johannes.catprototype.R;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class CatMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private Marker marker;
    //private String markerInfo;
    private FragmentActivity myContext;
    private ArrayAdapter<Marker> places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.cat_map_fragment,
                container, false);
        Button menuButton = (Button) view.findViewById(R.id.cat_map_back_button);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCat();
            }
        });

        initMaps();

        return view;

    }

    private void initMaps() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            AlertDialog alert = new AlertDialog.Builder(getActivity()).setTitle("Nie spe³niasz wymagañ aplikacji!").setCancelable(false).setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    toCat();
                }
            }).setMessage("Ta czêœæ aplikacji wymaga dostêpu do Twojej aktualnej lokalizacji. SprawdŸ, czy masz w³¹czon¹ funkcje lokalizacja w ustawieniach systemowych. Po tym wróæ i ponów wykonywanie tej operacji.").show();
        } else {

            if (map == null) {
                // Try to obtain the map from the SupportMapFragment.
                map = ((SupportMapFragment) myContext.getSupportFragmentManager().findFragmentById(R.id.google_maps_fragment)).getMap();

                // Check if we were successful in obtaining the map.
                if (map != null) {
                    //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                    map.setMyLocationEnabled(true);

                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            Log.d("upd", "updatelocation");
                            //map.clear();
                            if(marker!=null) {
                                marker.remove();
                            }
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            MarkerOptions mOption = new MarkerOptions().position(latLng).title("Twoja lokalizacja").flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                            marker = map.addMarker(mOption);
                            //markerInfo = marker.getPosition().toString();
                        }
                    });
                }
            }
        }
    }

    private void addPlace(LatLng position,String description) {
        MarkerOptions mOption = new MarkerOptions().position(position).title(description).flat(true);
        marker = map.addMarker(mOption);
        places.add(marker);
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
}
