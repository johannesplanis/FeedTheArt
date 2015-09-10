package menuactivity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class MenuFragment extends Fragment {
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
        SharedPreferences shared = act.getSharedPreferences("INSTANCES_COUNT", act.MODE_PRIVATE);

        //check if app is opened for the first time
        int COUNT = shared.getInt("COUNT",0);
        if(COUNT==0){
            continueButton.setVisibility(View.INVISIBLE);
        }
        TextView tv = (TextView) view.findViewById(R.id.menu_title_text_field);
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        tv.setText("Feed The Art!");
        tv.setTypeface(customFont);
        return view;


    }
    public void checkIfFirstTime(){
        Activity act = getActivity();
        SharedPreferences shared = act.getSharedPreferences("INSTANCES_COUNT", act.MODE_PRIVATE);
        int COUNT = shared.getInt("COUNT",0);
        if(COUNT==0){

        }
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
