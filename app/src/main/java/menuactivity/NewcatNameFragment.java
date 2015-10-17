package menuactivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import modules.BusModule;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class NewcatNameFragment extends Fragment {

    @Bind(R.id.newcat_name_input_field)
    EditText nameInputField;
    @Bind(R.id.newcat_name_finish)
    Button finishButton;
    @Bind(R.id.newcat_name_back)
    Button backButton;
    @Bind(R.id.newcat_name_relative_layout)
    RelativeLayout relativeLayout;



    @Inject
    protected Bus bus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.menu_newcat_name_fragment,
                container, false);

        ButterKnife.bind(this, view);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCreator(nameInputField.getText().toString().trim());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nameToChoose();
            }
        });
        setupKeyboard(view);
        return view;
    }

    //hide keyboard by touching outside input field
    public void setupKeyboard(View view){
        relativeLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                return false;
            }
        });

        nameInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    finishCreator(nameInputField.getText().toString().trim());
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        //dependency injection games
        BusModule.getObjectGraph().inject(this);
        int character = getCharacter();


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

    public void finishCreator(String name){

        Activity act = getActivity();
        if (act instanceof MenuActivity)
           ((MenuActivity) act).finishCreator(name);
    }

    public void nameToChoose(){
        Activity act = getActivity(); if (act instanceof MenuActivity)
            ((MenuActivity) act).nameToChoose();
    }

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
    private int getCharacter(){
        int character = 4;
        Activity act = getActivity(); if (act instanceof MenuActivity)
            character = ((MenuActivity) act).getCharacter();
            checkCharacter(character);
            return character;
    }




}
