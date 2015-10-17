package catactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class CatSplashFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("planis", "onCreate SplashFragment. Saved state? "
                + (savedInstanceState != null));
        final View view = inflater.inflate(R.layout.splash_fragment,
                container, false);
        TextView tv = (TextView) view.findViewById(R.id.splash_text_center);

        //http://stackoverflow.com/questions/27588965/how-to-use-custom-font-in-android-studio
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        tv.setText("Feed The Art!");
        tv.setTypeface(customFont);
        return view;


    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    @Override
    public void onDetach() {
        super.onDetach();

    }
}
