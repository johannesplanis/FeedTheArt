package fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.feedtheart.bambino.R;


public class CatSplashFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.splash_fragment,
                container, false);
        TextView tv = (TextView) view.findViewById(R.id.splash_text_center);
        Typeface customFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/AustieBostKittenKlub.ttf");
        tv.setText(R.string.feed_the_art_i);
        tv.setTypeface(customFont);
        return view;
    }
}
