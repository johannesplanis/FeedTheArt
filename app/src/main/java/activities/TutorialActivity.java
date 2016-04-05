package activities;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.planis.johannes.feedtheart.bambino.R;

public class TutorialActivity extends AppIntro2 {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance("Kochasz sztukę?\nMy też!", "Feed The Art to aplikacja, która pozwoli Ci czerpać ze sztuki znacznie więcej - rozrywkę.\nPoznaj w kilku krokach naszą aplikację.", R.drawable.logo, Color.parseColor("#000000")));
        addSlide(AppIntroFragment.newInstance("Dobierz kota ;)", "Na początku Twojej zabawy ze sztuką musisz wybrać wygląd kota, które będzie Ci towarzyszył. Mamy dla Ciebie wiele możliwości wyboru ;) Wkrótce sam się przekonasz!", R.drawable.cat_painter, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Dbaj o niego!", "Kotek lubi, gdy masz styczność ze sztuką. Jeżeli zaniedbasz swoje kulturalne życie jest on smutny. Rozwesel go czytając sztukę na ten dzień lub odwiedzając najbliższe muzeum ;)", R.drawable.cat_painter_sad, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Graj i odkrywaj!", "To już czas, aby wyruszyć w miasto i cieszyć się swoim kotem ;)\nBaw się dobrze.", R.drawable.map_tutorial, Color.parseColor("#3F51B5")));


    }


    @Override
    public void onDonePressed() {

        finish();
    }
}