package menuactivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.planis.johannes.catprototype.R;

/**
 * Created by JOHANNES on 8/6/2015.
 */
public class MenuSettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_settings_fragment,
                container, false);

        Button backButton = (Button) view.findViewById(R.id.menu_settings_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenu();
            }
        });
        return view;
    }

    public void toMenu(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toMenu();
    }
}
