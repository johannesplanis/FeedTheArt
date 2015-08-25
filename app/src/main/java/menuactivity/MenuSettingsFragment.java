package menuactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOHANNES on 8/6/2015.
 */
public class MenuSettingsFragment extends Fragment {

    @Bind(R.id.settings_reset_progress)
    TextView reset;
    @Bind(R.id.menu_settings_header_field)
    TextView head;
    @Bind(R.id.menu_settings_back_button)
    Button backButton;

    Typeface customFont;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_settings_fragment,
                container, false);

        ButterKnife.bind(this, view);


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

    }

    public void resetProgress(){

    }



}
