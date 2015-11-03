package tutorial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.planis.johannes.feedtheart.bambino.R;

import menuactivity.MenuActivity;

public class TutorialActivity extends AppIntro2 {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        /*
        addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);
        */
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Kochasz sztukę?\nMy też!", "Feed The Art to aplikacja, która pozwoli Ci czerpać ze sztuki znacznie więcej - rozrywkę.\nPoznaj w kilku krokach naszą aplikację.", R.drawable.logo, Color.parseColor("#000000")));
        addSlide(AppIntroFragment.newInstance("Dobierz kota ;)", "Na początku Twojej zabawy ze sztuką musisz wybrać wygląd kota, które będzie Ci towarzyszył. Mamy dla Ciebie wiele możliwości wyboru ;) Wkrótce sam się przekonasz!", R.drawable.cat_painter, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Dbaj o niego!", "Kotek lubi, gdy masz styczność ze sztuką. Jeżeli zaniedbasz swoje kulturalne życie jest on smutny. Rozwesel go czytając sztukę na ten dzień lub odwiedzając najbliższe muzeum ;)", R.drawable.cat_painter_sad, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Graj i odkrywaj!", "To już czas, aby wyruszyć w miasto i cieszyć się swoim kotem ;)\nBaw się dobrze.", R.drawable.map_tutorial, Color.parseColor("#3F51B5")));

        // OPTIONAL METHODS
        // Override bar/separator color
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        /*
        setVibrate(true);
        setVibrateIntensity(30);
        */
    }

    /*
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        AlertDialog dialog = new AlertDialog.Builder(TutorialActivity.this).setTitle("Are you sure?").setMessage("If you skip tutorial maybe you don't know what you can do in Feed The Art application.").setPositiveButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //---
                //Toast.makeText(TutorialActivity.this,"skip ;(",Toast.LENGTH_SHORT).show();

                //saveMessageToLocalStorageAboutCloseTutorial
                /*
                Intent tutorial=new Intent(TutorialActivity.this, MenuActivity.class);
                startActivity(tutorial);
                */
    /*
            finish();
        }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //---
            }
        }).show();

    }
    */

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        //Toast.makeText(TutorialActivity.this,"Koniec ;)",Toast.LENGTH_SHORT).show();

        //saveMessageToLocalStorageAboutCloseTutorial
        /*
        Intent tutorial=new Intent(TutorialActivity.this, MenuActivity.class);
        startActivity(tutorial);
        */
        finish();
    }
}