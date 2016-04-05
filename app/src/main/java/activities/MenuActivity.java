

package activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planis.johannes.feedtheart.bambino.R;

import controllers.SettingsController;
import controllers.SharedPreferencesController;
import fragments.MenuFragment;
import fragments.MenuSettingsFragment;
import fragments.NewcatChooseFragment;
import fragments.NewcatNameFragment;
import fragments.SplashFragment;
import model.Cat;
import model.Constants;
import model.Tags;

/*
* ISSUE 1: one fragment after being replaced by another, remains clickable
* and navigates like it was still present, even after being hidden or removed.
* SOLUTION: add android:clickable="true" property to layout files for every fragment.
* Why ?!? Probably it removes all background fragments' clickability.
* */

public class MenuActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private SharedPreferencesController spc;
    private Handler handler = new Handler();

    private static final int SPLASH = 1;
    private static final int MENU = 2;
    private static final int NEWCAT_CHOOSE = 3;
    private static final int NEWCAT_NAME = 4;
    private static final int SETTINGS = 5;


    private int fragmentType = SPLASH;


    private android.support.v4.app.Fragment getFragment() {
        switch (fragmentType) {
            case SPLASH:
                return new SplashFragment();
            case MENU:
                return new MenuFragment();
            case NEWCAT_CHOOSE:
                return new NewcatChooseFragment();
            case NEWCAT_NAME:
                return new NewcatNameFragment();
            case SETTINGS:
                return new MenuSettingsFragment();
            default:
                return new MenuFragment();
        }
    }

    private void changeFragment(int fragmentToLoad) {

        fragmentType = fragmentToLoad;
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, getFragment()).commit();
    }


    Cat cat;
    Gson gson;

    public int character;
    public String name;

    Boolean exit = false;

    public static final String APP = "APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState != null) {
            character = savedInstanceState.getInt(Tags.CHARACTER);
            name = savedInstanceState.getString(Tags.NAME);
        }
        spc = new SharedPreferencesController(getApplicationContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        exit = intent.getBooleanExtra("EXIT", false);

        setupPreferences();
        startup();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            toCat("MENU_ACTIVITY");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    if (exit) {
                        finish();
                        System.out.println("Trouble!");
                    } else {
                        toCat("MENU_ACTIVITY");

                    }
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle out) {
        out.putInt(Tags.CHARACTER, character);
        out.putString(Tags.NAME, name);
        gson = new Gson();
        String json = gson.toJson(cat);
        out.putString(Tags.CAT, json);
        super.onSaveInstanceState(out);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        name = savedState.getString(Tags.NAME);
        character = savedState.getInt(Tags.CHARACTER);
        String json = savedState.getString(Tags.CAT);
        gson = new Gson();
        cat = gson.fromJson(json, Cat.class);
    }

    @Override
    public void onBackPressed() {

        if (fragmentType == MENU) {
            finish();
        } else if (fragmentType == NEWCAT_NAME) {
            changeFragment(NEWCAT_CHOOSE);
        } else {
            changeFragment(MENU);
        }

    }


    /*
    Startup methods
     */
    public void startup() {
        spc = new SharedPreferencesController(getApplicationContext());


        toMenu();

    }

    /**
     * stup default shared preferences once and only once
     */
    private void setupPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor ed = getSharedPreferences(SettingsController.SETTINGS_PREFS, MODE_PRIVATE).edit();
            ed.putString(SettingsController.SETTINGS_STARVING_SPEED_COEFF, SettingsController.DEFAULT_STARVING_SPEED);
            ed.putInt(SettingsController.SETTINGS_DEFAULT_ALARM_LEVELS_SIZE, 3);
            float[] alarmLevels = SettingsController.DEFAULT_ALARM_LEVELS;
            for (int i = 0; i < alarmLevels.length; i++) {
                ed.putFloat(SettingsController.SETTINGS_DEFAULT_ALARM_LEVELS + "_" + i, alarmLevels[i]);
            }
            ed.commit();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

    public void splashStartup() {

        getSupportFragmentManager().beginTransaction().add(R.id.menu_container, getFragment()).commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMenu();
            }
        }, 3000);
    }

    /**
     * Navigate to Menu from all possible locations
     */
    public void toMenu() {
        changeFragment(MENU);

    }

    /**
     * Menu navigation methods
     */

    public void continueGame() {
        toCatActivity();
        //finish();
    }

    /**
     * start cat creator
     */
    public void newCat() {

        changeFragment(NEWCAT_CHOOSE);
    }


    /**
     * navigate to tutorial
     */
    public void toTutorial() {

        Intent tutorial = new Intent(MenuActivity.this, TutorialActivity.class);
        startActivity(tutorial);

    }

    public void toSettings() {

        changeFragment(SETTINGS);
    }

    /*
    New cat creator navigation
     */
    public void chooseToName() {
        changeFragment(NEWCAT_NAME);
    }

    public void nameToChoose() {

        changeFragment(NEWCAT_CHOOSE);
    }

    //check if there was an input, update cat's name, finish creator
    public void finishCreator(String inputName) {

        Log.i("CREATOR", "2");
        if (inputName != null && !inputName.isEmpty() && !inputName.equals("SAY MY NAME!")) {
            setName(inputName);

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




            toCatActivity();


        } else {
            Toast.makeText(this, "Put something here!", Toast.LENGTH_LONG).show();
        }
    }

    /*
    Navigate to cat Activity
     */
    public void toCatActivity() {

        checkPermissions();
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }


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

    public void popBackstack() {

        getFragmentManager().popBackStack();

    }
}

