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
 * Created by JOHANNES on 8/5/2015.
 */
public class NewcatChooseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_newcat_choose_fragment,
                container, false);

        Button nextButton = (Button) view.findViewById(R.id.newcat_choose_next);
        Button backButton = (Button) view.findViewById(R.id.newcat_choose_back);
       nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseToName();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenu();
            }
        });
        return view;
    }

    public void chooseToName(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).chooseToName();
    }
    public void toMenu(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toMenu();
    }
}
