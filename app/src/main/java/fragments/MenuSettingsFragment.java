package fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

import activities.MenuActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.Tags;
import controllers.SettingsController;

/**
 * Created by JOHANNES on 8/6/2015.
 */
public class MenuSettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{

    @Bind(R.id.settings_reset_progress)
    TextView reset;
    @Bind(R.id.menu_settings_header_field)
    TextView head;
    @Bind(R.id.menu_settings_back_button)
    Button backButton;
    @Bind(R.id.settings_starving_speed)
    SeekBar starvingSpeedBar;
    @Bind(R.id.settings_starving_speed_percent)
    TextView starvingSpeedPercent;
    @Bind(R.id.settings_notification_permission_switch)
    Switch mSwitch;


    Typeface customFont;
    SettingsController sc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_settings_fragment,
                container, false);

        ButterKnife.bind(this, view);

        setupLayout();

        return view;
    }

    public void toMenu() {
        Activity act = getActivity();
        if (act instanceof MenuActivity) {
            ((MenuActivity) act).toMenu();
            ((MenuActivity) act).popBackstack();
        }

    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        sc = new SettingsController(getActivity());
    }

    public void resetProgress(){

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        double fProgress = (double) progress;
        sc.setStarvingSpeed(fProgress*2/1000000);//adjust to look nice in settings page, and work as designed as well
        Log.i(Tags.SETTINGS,"Progress:"+progress);
        starvingSpeedPercent.setText(progress+" %");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //Handle orientation changes
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater,ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.menu_settings_fragment, viewGroup);
        ButterKnife.bind(this, subview);
        setupLayout();
    }

    private void setupLayout(){

        //SeekBar mSeekBar = (SeekBar) getView().findViewById(R.id.settings_starving_speed);
        starvingSpeedBar.setOnSeekBarChangeListener(this);
        //Switch mSwitch = (Switch) getView().findViewById(R.id.settings_notification_permission_switch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sc.setNotificationPermission(isChecked);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenu();
            }
        });
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetProgress();
            }
        });

        customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");

        double fspeed = sc.getStarvingSpeed()/2*1000000;
        starvingSpeedBar.setProgress((int)Math.ceil(fspeed)); //after loading settings, set current value of setting
        mSwitch.setChecked(sc.isNotificationPermission());
        starvingSpeedPercent.setText(fspeed+" %");

         }



}
