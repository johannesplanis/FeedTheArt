package intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;

import menuactivity.MenuActivity;
import menuactivity.SplashFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SplashFragment.instantiate(getApplicationContext(),"Slide1"));
        addSlide(SplashFragment.instantiate(getApplicationContext(),"Slide2"));
        addSlide(SplashFragment.instantiate(getApplicationContext(),"Slide3"));

    }


    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
