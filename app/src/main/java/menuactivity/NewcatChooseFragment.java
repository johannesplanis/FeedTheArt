package menuactivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class NewcatChooseFragment extends android.support.v4.app.Fragment {

    @Inject
    protected Bus bus;
    @Bind(R.id.newcat_choose_pager)
    ViewPager viewPager;
    @Bind(R.id.newcat_choose_next)
    TextView nextButton;
    @Bind(R.id.newcat_choose_back)
    TextView backButton;
    @Bind(R.id.newcat_choose_fragment_header)
    TextView chooseCharacterHeader;

    CharacterPagerAdapter adapterViewPager;
    private int character;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_newcat_choose_fragment,
                container, false);
        ButterKnife.bind(this,view);

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
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        chooseCharacterHeader.setTypeface(customFont);
        return view;
    }
    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        //dependency injection games
        //BusModule.getObjectGraph().inject(this);

    }
    /*
    set adapter on last page visited
     */
    @Override
    public void onResume(){
        super.onResume();
        //bus.register(sendEventHandler);
        Activity act = getActivity(); if (act instanceof MenuActivity)
        character = ((MenuActivity) act).getCharacter();
        viewPager.setCurrentItem(character);
    }
    @Override
    public void onPause(){
        super.onPause();
        //bus.unregister(sendEventHandler);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
       super.onActivityCreated(savedInstance);
        adapterViewPager = new CharacterPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);

    }


    public void chooseToName(){
        //get chosen character and put into variable in Activity
        int item = viewPager.getCurrentItem();
        Activity act = getActivity(); if(act instanceof MenuActivity)
            ((MenuActivity) act).setCharacter(item);

            ((MenuActivity) act).chooseToName();





    }
    public void toMenu(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toMenu();
    }
/*
    private Object sendEventHandler = new Object(){
        @Produce
        public int produceEvent(){
            return viewPager.getCurrentItem();
        }

    };

*/

}
