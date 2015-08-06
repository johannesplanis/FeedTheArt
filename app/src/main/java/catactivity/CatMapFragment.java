package catactivity;

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
public class CatMapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.cat_map_fragment,
                container, false);
        Button menuButton = (Button) view.findViewById(R.id.cat_map_back_button);

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toCat();
            }
        });

        return view;

    }

    public void toCat(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toCat();
    }

}
