

package menuactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planis.johannes.catprototype.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import cat.Cat;
import cat.Tags;
import catactivity.CatActivity;
import controllers.SettingsController;
import controllers.SharedPreferencesController;
import modules.BusModule;

/*
* ISSUE 1: one fragment after being replaced by another, remains clickable
* and navigates like it was still present, even after being hidden or removed.
* SOLUTION: add android:clickable="true" property to layout files for every fragment.
* Why ?!? Probably it removes all background fragments' clickability.
* */

public class MenuActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener {
    public SplashFragment spf;
    public MenuFragment mef;
    public TutorialFragment tuf;
    public NewcatChooseFragment ncf;
    public NewcatNameFragment nnf;
    public MenuSettingsFragment msf;
    public SharedPreferences shared;
    private SharedPreferencesController spc;
    private int COUNT;
    private Handler handler = new Handler();
    Cat cat;
    Gson gson;


    public int character;
    public String name;

    @Inject
    protected Bus bus;

    public static final String APP = "APP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //dependency injection games
        BusModule.getObjectGraph().inject(this);

        if(savedInstanceState != null){
            character = savedInstanceState.getInt(Tags.CHARACTER);
            name = savedInstanceState.getString(Tags.NAME);
        }
        spc = new SharedPreferencesController(getApplicationContext());

        Intent intent = getIntent();
        Boolean exit = intent.getBooleanExtra("EXIT",false);
        System.out.println(exit);
        if (exit==true){
            finish();
            System.out.println("Trouble!");
        } else{
            startup();
        }
        setupPreferences();

    }

    @Override
    public void onResume(){
        super.onResume();

        //BusProvider.getInstance().register(sendEventHandler);
    }

    @Override
    public void onPause(){
        super.onPause();
       // BusProvider.getInstance().unregister(sendEventHandler);
    }

    @Override
    protected void onSaveInstanceState(Bundle out){
        out.putInt(Tags.CHARACTER, character);
        out.putString(Tags.NAME, name);
        gson = new Gson();
        String json = gson.toJson(cat);
        out.putString(Tags.CAT,json);
        super.onSaveInstanceState(out);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState){
        name = savedState.getString(Tags.NAME);
        character = savedState.getInt(Tags.CHARACTER);
        String json = savedState.getString(Tags.CAT);
        gson = new Gson();
        cat = gson.fromJson(json, Cat.class);
    }
    /*
    Handling orientation changes. Shitty way, to be improved.
     */


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

    }

    /*
    Startup methods
     */
    public void startup(){
        spc = new SharedPreferencesController(getApplicationContext());
        Intent intent = getIntent();
        String type = intent.getStringExtra("TYPE");
        COUNT = spc.getInt(Tags.CAT_INSTANCES_COUNT,0);

        /*
        Handling different cases of first launch, non-first launch, going back to menu from Cat Activity
         */
        if(COUNT==0&&!APP.equals(type)){

            splashStartup();
        } else if(COUNT!=0&&APP.equals(type)){
            toMenu();
        } else if(COUNT!=0&&!APP.equals(type)){
            toCatActivity();
        } else{
            System.out.println("Error! Impossible test case!");
        }
    }
    /**
     * stup default shared preferences once and only once
     */
    private void setupPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor ed = getSharedPreferences(SettingsController.SETTINGS_PREFS, MODE_PRIVATE).edit();
            ed.putString(SettingsController.SETTINGS_STARVING_SPEED_COEFF, SettingsController.DEFAULT_STARVING_SPEED);
            ed.putInt(SettingsController.SETTINGS_DEFAULT_ALARM_LEVELS_SIZE,3);
            float[] alarmLevels = SettingsController.DEFAULT_ALARM_LEVELS;
            for(int i=0;i<alarmLevels.length;i++){
                ed.putFloat(SettingsController.SETTINGS_DEFAULT_ALARM_LEVELS + "_" + i, alarmLevels[i]);
            }
            ed.commit();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }
    public void splashStartup(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        spf = new SplashFragment();

        if(spf.isAdded()){
            ft.show(spf);
        } else{
            ft.add(R.id.menu_container, spf, Tags.SPLASH_FRAGMENT);
        }
        ft.commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMenu();
            }
        }, 3000);
    }
    /**
    Navigate to Menu from all possible locations
     */
    public void toMenu(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        mef = new MenuFragment();
        if(mef.isAdded()){
            ft.show(mef);
        } else{
            ft.add(R.id.menu_container, mef, Tags.MENU_FRAGMENT);
        }

        spf = (SplashFragment) getSupportFragmentManager().findFragmentByTag(Tags.SPLASH_FRAGMENT);
        if (spf!=null){
            ft.hide(spf);
        }
        tuf = (TutorialFragment) getSupportFragmentManager().findFragmentByTag(Tags.TUTORIAL_FRAGMENT);
        if(tuf!=null){
            ft.hide(tuf);
        }
        ncf = (NewcatChooseFragment) getSupportFragmentManager().findFragmentByTag(Tags.NEWCAT_CHOOSE_FRAGMENT);
        if(ncf!=null){
            ft.hide(ncf);
        }
        msf = (MenuSettingsFragment) getSupportFragmentManager().findFragmentByTag(Tags.SETTINGS_FRAGMENT);
        if(msf!=null){
            ft.hide(msf);
        }

        ft.commit();
    }
    /**
    Menu navigation methods
     */

    public void continueGame(){
        toCatActivity();
        //finish();
    }
    public void newCat(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ncf = new NewcatChooseFragment();
        if(ncf.isAdded()){
            ft.show(ncf);
        } else{
            ft.add(R.id.menu_container, ncf, Tags.NEWCAT_CHOOSE_FRAGMENT);
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag(Tags.MENU_FRAGMENT);
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack(Tags.NEWCAT_CHOOSE_FRAGMENT);
        ft.commit();
    }
    public void toTutorial(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        tuf = new TutorialFragment();
        if(tuf.isAdded()){
            ft.show(tuf);
        } else{
            ft.add(R.id.menu_container, tuf, Tags.TUTORIAL_FRAGMENT);
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag(Tags.MENU_FRAGMENT);
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack(Tags.TUTORIAL_FRAGMENT);
        ft.commit();
    }
    public void toSettings(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        msf = new MenuSettingsFragment();
        if(msf.isAdded()){
            ft.show(msf);
        } else{
            ft.add(R.id.menu_container,msf, Tags.SETTINGS_FRAGMENT);
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag(Tags.MENU_FRAGMENT);
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack(Tags.SETTINGS_FRAGMENT);
        ft.commit();
    }
    /*
    New cat creator navigation
     */
    public void chooseToName(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        nnf = new NewcatNameFragment();
        if(nnf.isAdded()){
            ft.show(nnf);
        } else{
            ft.add(R.id.menu_container,nnf, Tags.NEWCAT_NAME_FRAGMENT);
        }
        ncf = (NewcatChooseFragment) getSupportFragmentManager().findFragmentByTag(Tags.NEWCAT_CHOOSE_FRAGMENT);
        if(ncf!=null){
            ft.hide(ncf);
        }
        ft.addToBackStack(Tags.NEWCAT_NAME_FRAGMENT);
        ft.commit();
    }
    public void nameToChoose(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ncf = new NewcatChooseFragment();
        if(ncf.isAdded()){
            ft.show(ncf);
        } else{
            ft.add(R.id.menu_container, ncf, Tags.NEWCAT_CHOOSE_FRAGMENT);
        }
        nnf = (NewcatNameFragment) getSupportFragmentManager().findFragmentByTag(Tags.NEWCAT_NAME_FRAGMENT);
        if(nnf!=null){
            ft.hide(nnf);
        }
        ft.commit();
    }

    //check if there was an input, update cat's name, finish creator
    public void finishCreator(String inputName){
        Log.i("CREATOR","1");
        //spc = new SharedPreferencesController(getApplicationContext());
        Log.i("CREATOR","2");
        if(inputName!=null&&!inputName.isEmpty()&&!inputName.equals("SAY MY NAME!")){
            setName(inputName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            nnf = (NewcatNameFragment) getSupportFragmentManager().findFragmentByTag(Tags.NEWCAT_NAME_FRAGMENT);
            if(nnf!=null){
                ft.hide(nnf);
            }
            ft.commit();
            Log.i("CREATOR", "3");
        /*
        Indicate that new game was already created. Preference used in MenuFragment and startup();
         */
            SharedPreferences.Editor editor = getSharedPreferences("INSTANCES_COUNT", MODE_PRIVATE).edit();
            editor.putInt("COUNT", 1);
            editor.commit();
            Log.i("CREATOR", "4");
            spc.putInt(Tags.CAT_INSTANCES_COUNT, 1);
            //create Cat instance, put into separate shared preferences using gson under its ID as key
            //
            Cat cat = new Cat();//jeżeli zserializujemy z tym kontekstem to odczytując w drugiej activity będzie dupa
            cat.setName(inputName);
            cat.setCharacter(character);
            Log.i("CREATOR", "5");
            spc.putCat(Tags.CURRENT_GAME_INSTANCE, cat);
            Log.i("CREATOR", "6");
            toCatActivity();
            Log.i("CREATOR", "7");
        } else {
            Toast.makeText(this, "Put something here!", Toast.LENGTH_LONG).show();
        }
    }
    /*
    Navigate to cat Activity
     */
    public void toCatActivity(){
        Intent intent = new Intent(getApplicationContext(), CatActivity.class);
        intent.putExtra("START_MODE", "MENU_ACTIVITY");
        startActivity(intent);
    }

    /*
    Test if database works correctly
     */
    /*
    Otto tests
     */
    private Object sendEventHandler = new Object(){
        @Produce
        public String produceEvent(){
            return "Fuck fuck yeah!";
        }
        @Subscribe
        public void localTest(String msg){
            Log.i("Otto","Local "+msg);
        }
    };




    public void testDatabase(){

        //BusProvider.getInstance().post("Fuck yeah!");
        bus.post("Fuck yeah, injection!");
        Log.i("Otto", "test database!");

    }



    //class to carry the message
    public class MessageAvailableEvent{
        private String msg;
        MessageAvailableEvent(){
            this.msg = "Fuck yeah!";
        }

        public String getMsg(){
            return msg;
        }
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }
    public void setName(String name){this.name = name;}


    /**
     * settings section
     */


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

