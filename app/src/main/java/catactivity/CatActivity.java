package catactivity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.planis.johannes.catprototype.R;

import menuactivity.MenuActivity;
import menuactivity.SplashFragment;


public class CatActivity extends Activity {

    public CatFragment catf;
    public CatArtFragment artf;
    public CatMapFragment mapf;
    public SplashFragment splash;
    public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        startup();
    }
    @Override
    protected void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
    }
    /*
    handling orientation changes
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //go to main activity and exit
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
    /*
    startup methods, navigate to cat
     */
    public void startup(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        splash = new SplashFragment();

        if(splash.isAdded()){
            ft.show(splash);
        } else{
            ft.add(R.id.cat_container, splash, "SPLASH");
        }
        ft.commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toCat();
            }
        }, 1000);
    }
    public void toCat(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        catf = new CatFragment();

        if(catf.isAdded()){
            ft.show(catf);
        } else{
            ft.add(R.id.cat_container, catf, "CATF");
        }
        mapf = (CatMapFragment) getFragmentManager().findFragmentByTag("MAPF");
        if(mapf!=null){
            ft.hide(mapf);
        }

        artf = (CatArtFragment) getFragmentManager().findFragmentByTag("ARTF");
        if(artf!=null){
            ft.hide(artf);
        }

        ft.commit();
    }
    /*
    methods to navigate to fragments
     */

    public void toMenu(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("TYPE","APP");
        startActivity(intent);

    }
    public void toMap(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mapf = new CatMapFragment();
        if(mapf.isAdded()){
            ft.show(mapf);
        } else{
            ft.add(R.id.cat_container, mapf, "MAPF");
        }
        catf = (CatFragment) getFragmentManager().findFragmentByTag("CATF");
        if(catf!=null){
            ft.hide(catf);
        }
        ft.addToBackStack("MAP");
        ft.commit();
    }
    public void toArt(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        artf = new CatArtFragment();
        if(artf.isAdded()){
            ft.show(artf);
        } else{
            ft.add(R.id.cat_container, artf, "ARTF");
        }
        catf = (CatFragment) getFragmentManager().findFragmentByTag("CATF");
        if(catf!=null){
            ft.hide(catf);
        }
        ft.addToBackStack("ART");
        ft.commit();
    }
    public void toExtra(){

    }

    /*
    sharing art
     */
    public void shareArt(){

    }

}
