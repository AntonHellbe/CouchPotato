package se.mah.couchpotato.activitytvshow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class SeasonViewPagerAdapter extends FragmentStatePagerAdapter {

    private int seasons = 0;

    public SeasonViewPagerAdapter(FragmentManager fragmentManager, int seasons) {
        super(fragmentManager);
        this.seasons = seasons;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentSeason.newInstance(position);
    }

    @Override
    public int getCount() {
        return seasons;
    }
}
