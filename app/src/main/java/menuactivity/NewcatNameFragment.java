package menuactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.planis.johannes.catprototype.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import modules.BusModule;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class NewcatNameFragment extends Fragment {
    @Inject
    protected Bus bus;
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
    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        //dependency injection games
        BusModule.getObjectGraph().inject(this);
        getCharacter();
    }
    @Override
    public void onResume(){
        super.onResume();
        //bus.register(readEventHandler);

    }
    @Override
    public void onPause(){
        super.onPause();
        //bus.unregister(readEventHandler);
    }

    public void finishCreator(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).finishCreator();
    }
    public void nameToChoose(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).nameToChoose();
    }
/*
    private Object readEventHandler = new Object(){
        @Subscribe
        public void dataReceived(int data){

            checkCharacter(data);


        }
    };*/
    private void checkCharacter(int ID){
        switch(ID){
            case 0:
                Log.i("Name: ","Sweet cat");
                break;
            case 1:
                Log.i("Name: ","Evil cat");
                break;
            case 2:
                Log.i("Name: ","Pirate cat");
                break;
            default:
                Log.i("Name: ","noname");
                break;
        }
    }
    private void getCharacter(){
        int character = 4;
        Activity act = getActivity(); if (act instanceof MenuActivity)
            character = ((MenuActivity) act).getCharacter();
            checkCharacter(character);
    }
}
