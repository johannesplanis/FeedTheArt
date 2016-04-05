package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class SplashFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("planis", "onCreate SplashFragment. Saved state? "
                + (savedInstanceState != null));
        final View view = inflater.inflate(R.layout.splash_fragment,
                container, false);
        TextView tv = (TextView) view.findViewById(R.id.splash_text_center);

        tv.setText("Feed The Art!");
        return view;


    }

}
