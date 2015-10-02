package menuactivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by JOHANNES on 8/26/2015.
 */
public class CharacterPagerAdapter extends FragmentStatePagerAdapter {
    private final int mSize;


    public CharacterPagerAdapter(FragmentManager fm,int size){
        super(fm);
        mSize = size;
    }
    @Override
    public Fragment getItem(int position) {

        return NewcatChooseCharacterFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return mSize;
    }
}
