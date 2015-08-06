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
public class NewcatNameFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_newcat_name_fragment,
                container, false);

        Button finishButton = (Button) view.findViewById(R.id.newcat_name_finish);
        Button backButton = (Button) view.findViewById(R.id.newcat_name_back);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCreator();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameToChoose();
            }
        });
        return view;
    }

    public void finishCreator(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).finishCreator();
    }
    public void nameToChoose(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).nameToChoose();
    }
}
