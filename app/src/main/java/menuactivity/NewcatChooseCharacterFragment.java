package menuactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOHANNES on 8/26/2015.
 */
public class NewcatChooseCharacterFragment extends Fragment {


    @Bind(R.id.choose_character_fragment_header)
    TextView chooseHeader;
    private String name;
    private int id;


    public static NewcatChooseCharacterFragment newInstance(int id, String name){
        NewcatChooseCharacterFragment chooseCharacter = new NewcatChooseCharacterFragment();
        Bundle args = new Bundle();
        args.putInt("ID",id);
        args.putString("Name", name);
        chooseCharacter.setArguments(args);
        return chooseCharacter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("ID",0);
        name = getArguments().getString("Name", "noname");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menu_newcat_choose_character_fragment,container,false);
        ButterKnife.bind(this,view);
        chooseHeader.setText(name);
        return view;
    }
}
