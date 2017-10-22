package se.mah.couchpotato.activitytvshow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class SeasonViewPagerAdapter extends FragmentStatePagerAdapter {

    private int seasons = 0;
    private int position = -1;

    public SeasonViewPagerAdapter(FragmentManager fragmentManager, int seasons) {
        super(fragmentManager);
        this.seasons = seasons;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentSeason.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return seasons;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Season " + String.valueOf(position + 1);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != this.position) {
            FragmentSeason fragment = (FragmentSeason) object;
            ScrollableViewPager pager = (ScrollableViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                this.position = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
}
