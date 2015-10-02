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
import com.planis.johannes.catprototype.R;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import cat.Constants;
import cat.Tags;

/**
 * Created by JOHANNES on 8/5/2015.
 */

public class CatFragment extends Fragment {

    @Bind(R.id.cat_name_field)
    TextView catDialogTop;
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



    public String catName;
    public Bitmap placeholderBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.cat_fragment, container, false);

        ButterKnife.bind(this,view);

        setupLayout();

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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter(Tags.UPDATE_FOODLEVEL_ACTION));
    }
    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
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


        Activity act = getActivity(); if (act instanceof  CatActivity)
            catName = ((CatActivity) act).getName();
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
        catDialogTop.setTypeface(customFont);
        catDialogBottom.setTypeface(customFont);
        catDialogTop.setText("Hello, it's me, " + catName + " !");


        int character = ((CatActivity) getActivity()).getCharacter();

        int resId = Constants.catImageResIds[character];
        Log.i("CAT FRAGMENT", "character: " + character);

        Glide.with(this).load(resId).into(imageView);
        //Picasso.with(getActivity()).load(resId).into(imageView);
        //Glide has better performance

    }

    /**
     *receive broadcasted value from service and put into relevant field
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



}
