package fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

import activities.MenuActivity;
import model.Tags;
import controllers.SharedPreferencesController;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class MenuFragment extends Fragment {
    SharedPreferencesController spc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.menu_fragment,
                container, false);
        Button continueButton = (Button) view.findViewById(R.id.continue_button);
        Button newcatButton = (Button) view.findViewById(R.id.new_cat_button);
        Button tutorialButton = (Button) view.findViewById(R.id.tutorial_button);
        Button settingsButton = (Button) view.findViewById(R.id.settings_button);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                continueGame();
            }
        });
        newcatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCat();
            }
        });
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTutorial();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSettings();
            }
        });
        /*
        Check if the game is already created
         */
        Activity act = getActivity();
        spc = new SharedPreferencesController(act.getApplicationContext());
        int COUNT = spc.getInt(Tags.CAT_INSTANCES_COUNT, 0);
        //check if app is opened for the first time
        if(COUNT==0){
            continueButton.setVisibility(View.INVISIBLE);
        }
        TextView tv = (TextView) view.findViewById(R.id.menu_title_text_field);
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        tv.setText("Feed The Art!");
        //tv.setTypeface(customFont);
        return view;
    }


    public void continueGame(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).continueGame();
    }
    public void newCat(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).newCat();
    }
    public void toTutorial(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toTutorial(); }
    public void toSettings(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toSettings();
    }
}
