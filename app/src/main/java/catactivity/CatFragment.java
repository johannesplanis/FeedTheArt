package catactivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planis.johannes.feedtheart.bambino.R;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import cat.Cat;
import cat.Constants;
import cat.Tags;

/**
 * Created by JOHANNES on 8/5/2015.
 */

public class CatFragment extends Fragment {

   // @Bind(R.id.cat_name_field)
   // TextView catDialogTop;
    @Bind(R.id.cat_feedme_text)
    TextView catDialogBottom;
    @Bind(R.id.cat_menu_button)
    Button menuButton;
    @Bind(R.id.cat_map_button)
    Button mapButton;
    @Bind(R.id.cat_art_button)
    Button artButton;
    @Bind(R.id.cat_extra_button)
    Button extraButton;
    @Bind(R.id.cat_placeholder_image_view)
    ImageView imageView;
    @Bind(R.id.cat_name_field)
    TextView dialog;

    int venueID;
    double value;
    public String catName;
    public Bitmap placeholderBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.cat_fragment, container, false);

        ButterKnife.bind(this, view);

        setupLayout();
        CatFragment frCtxt=this;
        return view;

    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Tags.UPDATE_FOODLEVEL_ACTION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(humourBroadcastReceiver,new IntentFilter("HUMOUR"));
    }
    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(humourBroadcastReceiver);
    }


    public void toMenu(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toMenu();
    }
    public void toMap(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toMap();
    }
    public void toArt(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toArt();
    }
    public void toExtra(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toExtra();
    }

    private void populateViewForOrientation(LayoutInflater inflater,ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.cat_fragment, viewGroup);
        ButterKnife.bind(this,subview);
        setupLayout();
    }

    public void setupLayout(){

        int humour;
        Activity act = getActivity(); if (act instanceof  CatActivity)
            catName = ((CatActivity) act).getName();
            value = ((CatActivity) act).getResult();

        if(value>= Cat.ALARM_LEVEL){
            humour = 0;
            dialog.setText(Constants.FEEDING_REACTION);
        } else if(value>=Cat.CRITICAL_LEVEL&&value<Cat.ALARM_LEVEL){
            humour = 3;
            dialog.setText(Constants.NUDGE_REACTION);
        } else{
            humour = 3;
            dialog.setText(Constants.STARVING_REACTION);
        }
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toMenu();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMap();
            }
        });
        artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toArt();
            }
        });
        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toExtra();
            }
        });
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        //catDialogTop.setTypeface(customFont);
        //catDialogBottom.setTypeface(customFont);
       // catDialogTop.setText("Hello, it's me, " + catName + " !");


        int character = ((CatActivity) getActivity()).getCharacter();
        //int humour = ((CatActivity) getActivity()).getHumour();
        int resId = Constants.catImageResIds[character+humour];
        Log.i("CAT FRAGMENT", "character: " + character);

        Glide.with(this).load(resId).into(imageView);
        //Picasso.with(getActivity()).load(resId).into(imageView);
        //Glide has better performance

    }

    /**
     *receive broadcasted value from service and put into relevant field
     * TODO: wyświetlanie dialogu w zależności od humoru i geofence;
     */
     private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            catDialogBottom.requestLayout();

            DecimalFormat formatter = new DecimalFormat("###.#");
            double result = intent.getDoubleExtra("SERVICE_BROADCAST", 0);
            /*
            double prevResult = ((CatActivity) getActivity()).getResult();

            if(result>prevResult){
                //case when cat was fed
                if(result>= Cat.NUDGE_LEVEL){
                    Glide.with(getActivity()).load(Constants.catImageResIds[((CatActivity) getActivity()).getCharacter()]).into(imageView);
                    Log.i("HUMOUR", "HAPPING");
                }
            } else if(prevResult>=Cat.NUDGE_LEVEL&&result<Cat.NUDGE_LEVEL){
            //case when cat starved below safe level
            Glide.with(context).load(Constants.catImageResIds[((CatActivity) getActivity()).getCharacter()+3]).into(imageView);
            Log.i("HUMOUR","SADDING");}
            ((CatActivity)getActivity()).putResult(result);
*/

            String output = formatter.format(result);
            Log.i("CAT_FRAGMENT_RECEIVER", output);
            venueID = intent.getIntExtra("REQ_ID",0);
            if(venueID!=0) {
                Log.i("GEOFENCES_RECEIVER", "" + venueID); //get value of geofence id not equal to 0
                //catDialogTop.setText("" + venueID);
            }
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
            int character = ((CatActivity) getActivity()).getCharacter();
            String humour = intent.getStringExtra("HUMOUR");
            Log.i("HUMOUR","received");
            if(humour.equals("HUNGRING")){
                Log.i("HUMOUR", "hungring");
                int type = ((CatActivity) getActivity()).getCharacter();
                int resId = Constants.catImageResIds[type+3];
                Glide.with(CatFragment.this).load(resId).into(imageView);
                dialog.setText(Constants.NUDGE_REACTION);
            }else if(humour.equals("FEEDING")){

                Log.i("HUMOUR", "feeding");
                int type = ((CatActivity) getActivity()).getCharacter();
                int resId = Constants.catImageResIds[type];
                Glide.with(CatFragment.this).load(resId).into(imageView);
                dialog.setText(Constants.FEEDING_REACTION);
            }else if(humour.equals("FEEDING_FROM_STARVE")) {
                dialog.setText(Constants.NUDGE_REACTION);

            }else if(humour.equals("STARVING")){
                Log.i("HUMOUR", "starving");
                int type = ((CatActivity) getActivity()).getCharacter();
                int resId = Constants.catImageResIds[type+3];
                Glide.with(CatFragment.this).load(resId).into(imageView);
                dialog.setText(Constants.STARVING_REACTION);
            }
            else{
                Log.i("HUMOUR","none");
            }
        }
    };
}