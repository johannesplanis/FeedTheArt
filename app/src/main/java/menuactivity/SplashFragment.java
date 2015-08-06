package menuactivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planis.johannes.catprototype.R;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class SplashFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        not yet useful
         */
        /*if(container == null){
            return null;
        }*/
        Log.i("planis", "onCreate ListFragment. Saved state? "
                + (savedInstanceState != null));
        final View view = inflater.inflate(R.layout.splash_fragment,
                container, false);

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
