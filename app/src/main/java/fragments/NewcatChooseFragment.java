package fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

import activities.MenuActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import menuactivity.CharacterPagerAdapter;
import model.Constants;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class NewcatChooseFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.newcat_choose_pager)
    ViewPager viewPager;
    @Bind(R.id.newcat_choose_next)
    TextView nextButton;
    @Bind(R.id.newcat_choose_back)
    TextView backButton;
    @Bind(R.id.newcat_choose_fragment_header)
    TextView chooseCharacterHeader;
    @Bind(R.id.newcat_choose_swipe_l)
    TextView swipeL;
    @Bind(R.id.newcat_choose_swipe_r)
    TextView swipeR;


    CharacterPagerAdapter adapterViewPager;
    private int character;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_newcat_choose_fragment,
                container, false);
        ButterKnife.bind(this, view);

        setupLayout();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume(){
        super.onResume();
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
        adapterViewPager = new CharacterPagerAdapter(getChildFragmentManager(), Constants.catImageResIds0.length);
        viewPager.setAdapter(adapterViewPager);
    }


    private void setupLayout(){
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
        swipeL.setTypeface(customFont);
        swipeR.setTypeface(customFont);
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



}
