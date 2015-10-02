package menuactivity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cat.Constants;
import modules.BusModule;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class TutorialFragment extends Fragment {


    @Bind(R.id.objectives_field)
    TextView header;
    @Bind(R.id.tutorial_objectives_list_field)
    TextView tutorial;
    @Bind(R.id.tutorial_back_button)
    Button backButton;

    Typeface customFont;

    @Inject
    protected Bus bus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_tutorial_fragment,
                container, false);
        ButterKnife.bind(this,view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenu();
            }
        });
        customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), Constants.AUSTIE_BOST_KITTEN_KLUB_FONT);
        tutorial.setText(Constants.TUTORIAL);
        header.setTypeface(customFont);
        backButton.setTypeface(customFont);

        return view;
    }

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        BusModule.getObjectGraph().inject(this);

    }
    @Override
    public void onResume(){
        super.onResume();
        bus.register(readEventHandler);
        testDatabase();
    }
    @Override
    public void onPause(){
        super.onPause();
        bus.unregister(readEventHandler);
    }



    public void toMenu(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).toMenu();
    }

    public void testDatabase(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).testDatabase();
    }


    //Otto tests
    //https://github.com/StephenAsherson/Android-OttoSample/blob/master/src/com/stephenasherson/ottosample/contact/ContactSummaryFragment.java
    private Object readEventHandler = new Object(){
        @Subscribe public void dataReceived(String data){
            Log.i("Otto","External: "+data);
            TextView tv = (TextView) getView().findViewById(R.id.tutorial_objectives_list_field);
            //tv.setText(data);

        }
    };




}
