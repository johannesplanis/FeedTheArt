package menuactivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by JOHANNES on 8/26/2015.
 */
public class CharacterPagerAdapter extends FragmentStatePagerAdapter {

    private static int PAGES_NUM = 3;

    public CharacterPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return NewcatChooseCharacterFragment.newInstance(0, "Sweet Cat");
            case 1:
                return NewcatChooseCharacterFragment.newInstance(1, "Evil Cat");
            case 2:
                return NewcatChooseCharacterFragment.newInstance(2, "Pirate Cat");
            default:
                return NewcatChooseCharacterFragment.newInstance(position, "noname");

        }

    }

    @Override
    public int getCount() {
        return PAGES_NUM;
    }
}
