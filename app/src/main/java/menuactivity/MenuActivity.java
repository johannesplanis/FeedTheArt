

package menuactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.planis.johannes.catprototype.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import catactivity.CatActivity;
import modules.BusModule;

/*
* ISSUE 1: one fragment after being replaced by another, remains clickable
* and navigates like it was still present, even after being hidden or removed.
* SOLUTION: add android:clickable="true" property to layout files for every fragment.
* Why ?!? Probably it removes all background fragments' clickability.
* */

public class MenuActivity extends FragmentActivity {
    public SplashFragment spf;
    public MenuFragment mef;
    public TutorialFragment tuf;
    public NewcatChooseFragment ncf;
    public NewcatNameFragment nnf;
    public MenuSettingsFragment msf;
    public SharedPreferences shared;
    private int COUNT;
    private Handler handler = new Handler();

    private static final String CHARACTER = "CHARACTER";
    public int character;

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
            character = savedInstanceState.getInt(CHARACTER);
        }

        Intent intent = getIntent();
        Boolean exit = intent.getBooleanExtra("EXIT",false);
        System.out.println(exit);
        if (exit==true){
            finish();
            System.out.println("Trouble!");
        } else{
            startup();
        }


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
        out.putInt(CHARACTER,character);

        super.onSaveInstanceState(out);

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

        Intent intent = getIntent();
        String type = intent.getStringExtra("TYPE");


        shared = getSharedPreferences("INSTANCES_COUNT", MODE_PRIVATE);
        COUNT = shared.getInt("COUNT",0);

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
    public void splashStartup(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        spf = new SplashFragment();

        if(spf.isAdded()){
            ft.show(spf);
        } else{
            ft.add(R.id.menu_container, spf, "SPF");
        }
        ft.commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMenu();
            }
        }, 3000);
    }
    /*
    Navigate to Menu from all possible locations
     */
    public void toMenu(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //FragmentTransaction ft = getFragmentManager().beginTransaction();

        mef = new MenuFragment();
        if(mef.isAdded()){
            ft.show(mef);
        } else{
            ft.add(R.id.menu_container, mef, "MEF");
        }

        spf = (SplashFragment) getSupportFragmentManager().findFragmentByTag("SPF");
        if (spf!=null){
            ft.hide(spf);
        }
        tuf = (TutorialFragment) getSupportFragmentManager().findFragmentByTag("TUF");
        if(tuf!=null){
            ft.hide(tuf);
        }
        ncf = (NewcatChooseFragment) getSupportFragmentManager().findFragmentByTag("NCF");
        if(ncf!=null){
            ft.hide(ncf);
        }

        ft.commit();
    }
    /*
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
            ft.add(R.id.menu_container, ncf, "NCF");
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag("MEF");
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack("NEWCAT");
        ft.commit();
    }
    public void toTutorial(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        tuf = new TutorialFragment();
        if(tuf.isAdded()){
            ft.show(tuf);
        } else{
            ft.add(R.id.menu_container,tuf,"TUF");
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag("MEF");
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack("TUTORIAL");
        ft.commit();
    }
    public void toSettings(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        msf = new MenuSettingsFragment();
        if(msf.isAdded()){
            ft.show(msf);
        } else{
            ft.add(R.id.menu_container,msf,"MSF");
        }
        mef = (MenuFragment) getSupportFragmentManager().findFragmentByTag("MEF");
        if(mef!=null){
            ft.hide(mef);
        }
        ft.addToBackStack("SETTINGS");
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
            ft.add(R.id.menu_container,nnf,"NNF");
        }
        ncf = (NewcatChooseFragment) getSupportFragmentManager().findFragmentByTag("NCF");
        if(ncf!=null){
            ft.hide(ncf);
        }
        ft.addToBackStack("CHOOSETONAME");
        ft.commit();
    }
    public void nameToChoose(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ncf = new NewcatChooseFragment();
        if(ncf.isAdded()){
            ft.show(ncf);
        } else{
            ft.add(R.id.menu_container,ncf,"NCF");
        }
        nnf = (NewcatNameFragment) getSupportFragmentManager().findFragmentByTag("NNF");
        if(nnf!=null){
            ft.hide(nnf);
        }
        ft.commit();
    }
    public void finishCreator(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        nnf = (NewcatNameFragment) getSupportFragmentManager().findFragmentByTag("NNF");
        if(nnf!=null){
            ft.hide(nnf);
        }
        /*
        Indicate that new game was already created. Preference used in MenuFragment and startup();
         */
        SharedPreferences.Editor editor = getSharedPreferences("INSTANCES_COUNT", MODE_PRIVATE).edit();
        editor.putInt("COUNT",1);
        editor.commit();
        toCatActivity();
    }
    /*
    Navigate to cat Activity
     */
    public void toCatActivity(){
        Intent intent = new Intent(getApplicationContext(), CatActivity.class);
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
        Log.i("Otto","test database!");


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
}
