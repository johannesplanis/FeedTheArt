package menuactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planis.johannes.feedtheart.bambino.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cat.Constants;

/**
 * TODO handle image loading on orientation change
 * Created by JOHANNES on 8/26/2015.
 */
public class NewcatChooseCharacterFragment extends Fragment {


    @Bind(R.id.choose_character_fragment_header)
    TextView chooseHeader;
    @Bind(R.id.choose_character_fragment_image)
    ImageView imageView;

    private int id;


    public static NewcatChooseCharacterFragment newInstance(int id){
        NewcatChooseCharacterFragment chooseCharacter = new NewcatChooseCharacterFragment();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        //args.putString("Name", name);
        chooseCharacter.setArguments(args);
        return chooseCharacter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        id = getArguments()!=null ? getArguments().getInt("ID"):-1;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.menu_newcat_choose_character_fragment,container,false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        final int resId = Constants.catImageResIds0[id];
        final String resName = Constants.catsNames[id];

        //Glide has better performance than Picasso and Google's example for handling bitmaps
        Glide.with(this).load(resId).into(imageView);
        chooseHeader.setText(resName);
    }
}
