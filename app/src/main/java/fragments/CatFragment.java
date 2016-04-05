package fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

import java.text.DecimalFormat;

import activities.CatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geofencing.VenuesDevelopmentMode;
import model.Cat;
import model.Constants;
import model.Tags;

/**
 * todo jeżeli wychodzimy z artfragment przez
 * Created by JOHANNES on 8/5/2015.
 */

public class CatFragment extends Fragment {

    // @Bind(R.id.cat_name_field)
    // TextView catDialogTop;
    @Bind(R.id.cat_feedme_text)
    TextView catDialogBottom;
    @Bind(R.id.cat_menu_button)
    ImageView menuButton;
    @Bind(R.id.cat_map_button)
    TextView mapButton;
    @Bind(R.id.cat_art_button)
    TextView artButton;
    @Bind(R.id.cat_extra_button)
    TextView extraButton;
    @Bind(R.id.cat_placeholder_image_view)
    ImageView imageView;
    @Bind(R.id.cat_name_field)
    TextView dialog;

    CatActivity activity;


    String venueID;
    double value;
    public String catName;
    public Bitmap placeholderBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.cat_fragment, container, false);
        ButterKnife.bind(this, view);
        activity = (CatActivity) getActivity();
        setupLayout();

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Tags.UPDATE_FOODLEVEL_ACTION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(humourBroadcastReceiver, new IntentFilter("HUMOUR"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver1, new IntentFilter(Tags.REQ_ID));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(humourBroadcastReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver1);
    }


    @OnClick(R.id.cat_menu_button)
    void toMenu() {
        activity.toMenu();
    }

    @OnClick(R.id.cat_map_button)
    void toMap() {
        activity.toMap();
    }

    @OnClick(R.id.cat_art_button)
    void toArt() {
        activity.toArt();
    }

    @OnClick(R.id.cat_extra_button)
    void toExtra() {
        activity.toExtra();
    }

    public void setupLayout() {

        int humour;
        activity.getName();
        activity.getResult();

        if (value >= Cat.ALARM_LEVEL) {
            humour = 0;
            dialog.setText(Constants.FEEDING_REACTION);
        } else if (value >= Cat.CRITICAL_LEVEL && value < Cat.ALARM_LEVEL) {
            humour = 3;
            dialog.setText(Constants.NUDGE_REACTION);
        } else {
            humour = 3;
            dialog.setText(Constants.STARVING_REACTION);
        }

        int character = activity.getCharacter();
        int resId = Constants.catImageResIds[character + humour];

        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),resId));
    }


    /**
     * receive broadcasted value from service and put into relevant field
     * TODO: wyświetlanie dialogu w zależności od humoru i geofence;
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            catDialogBottom.requestLayout();

            DecimalFormat formatter = new DecimalFormat("###.#");
            double result = intent.getDoubleExtra("SERVICE_BROADCAST", 0);


            String output = formatter.format(result);
            Log.i("CAT_FRAGMENT_RECEIVER", output);

            catDialogBottom.setText(output);

        }
    };

    /**
     * broadcast receiver to change layout according to changing humour
     */
    private BroadcastReceiver humourBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            imageView.requestLayout();
            dialog.requestLayout();
            int character = activity.getCharacter();
            String humour = intent.getStringExtra("HUMOUR");
            Log.i("HUMOUR", "received");
            if (humour.equals("HUNGRING")) {
                Log.i("HUMOUR", "hungring");
                int type = activity.getCharacter();
                int resId = Constants.catImageResIds[type + 3];
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),resId));
                dialog.setText(Constants.NUDGE_REACTION);
            } else if (humour.equals("FEEDING")) {

                Log.i("HUMOUR", "feeding");
                int type = activity.getCharacter();
                int resId = Constants.catImageResIds[type];
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),resId));
                dialog.setText(Constants.FEEDING_REACTION);
            } else if (humour.equals("FEEDING_FROM_STARVE")) {
                dialog.setText(Constants.NUDGE_REACTION);

            } else if (humour.equals("STARVING")) {
                Log.i("HUMOUR", "starving");
                int type = activity.getCharacter();
                int resId = Constants.catImageResIds[type + 3];
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),resId));
                dialog.setText(Constants.STARVING_REACTION);
            } else {
                Log.i("HUMOUR", "none");
            }
        }
    };

    private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra(Tags.REQ_ID);
            String name = VenuesDevelopmentMode.sampleVenues().get(Integer.parseInt(id) - 1).getName();
            dialog.setText("I'm in " + name);
            Log.i("GEOFENCE_ID", id + name);
        }
    };

}