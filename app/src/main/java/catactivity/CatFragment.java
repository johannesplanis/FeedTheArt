package catactivity;

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
 * Created by JOHANNES on 8/5/2015.
 */

public class CatFragment extends Fragment {

    @Bind(R.id.cat_name_field)
    TextView catDialogTop;
    @Bind(R.id.cat_feedme_text)
    TextView catDialogBottom;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        not yet useful
         */
        /*if(container == null){
            return null;
        }*/

        final View view = inflater.inflate(R.layout.cat_fragment,
                container, false);
        ButterKnife.bind(this,view);
        Button menuButton = (Button) view.findViewById(R.id.cat_menu_button);
        Button mapButton = (Button) view.findViewById(R.id.cat_map_button);
        Button artButton = (Button) view.findViewById(R.id.cat_art_button);
        Button extraButton = (Button) view.findViewById(R.id.cat_extra_button);
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
        return view;

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


}
