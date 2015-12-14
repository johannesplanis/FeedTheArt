package activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fragments.CatArtFragment;
import fragments.CatFragment;
import fragments.CatMapFragment;
import fragments.CatSplashFragment;
import model.Constants;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toCat(String startMode){
        Intent intent = new Intent(this,CatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("START_MODE", startMode);
        startActivity(intent);
        finish();
    }
    public void toMap(){
        Intent intent = new Intent(this,MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,CatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public Fragment getFragment(String tag) throws Exception{
        switch (tag){
            case Constants.ART_FRAGMENT:
                return new CatArtFragment();
            case Constants.MAP_FRAGMENT:
                return new CatMapFragment();
            case Constants.CAT_FRAGMENT:
                return new CatFragment();
            case Constants.SPLASH_FRAGMENT:
                return new CatSplashFragment();
            default:
                return null;
        }
    }


}
