package menuactivity;

import android.app.Activity;
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

import com.planis.johannes.catprototype.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cat.Tags;

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

    Typeface customFont;
    SettingsController sc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_settings_fragment,
                container, false);

        ButterKnife.bind(this, view);


        SeekBar mSeekBar = (SeekBar) view.findViewById(R.id.settings_starving_speed);
        mSeekBar.setOnSeekBarChangeListener(this);
        Switch mSwitch = (Switch) view.findViewById(R.id.settings_notification_permission_switch);
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
        head.setTypeface(customFont);
        backButton.setTypeface(customFont);
        float fspeed = sc.getStarvingSpeed()/2*1000000;
        mSeekBar.setProgress(Math.round(fspeed)); //after loading settings, set current value of setting
        mSwitch.setChecked(sc.isNotificationPermission());

        return view;
    }

    public void toMenu() {
        Activity act = getActivity();
        if (act instanceof MenuActivity)
            ((MenuActivity) act).toMenu();
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

        float fProgress = (float) progress;
        sc.setStarvingSpeed(fProgress*2/1000000);//adjust to look nice in settings page, and work as designed as well
        Log.i(Tags.SETTINGS,"Progress:"+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
