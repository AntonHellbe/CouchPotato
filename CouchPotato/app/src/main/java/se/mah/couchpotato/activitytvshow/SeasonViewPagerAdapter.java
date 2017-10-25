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

    private int seasons = 1;
    private int position = -1;
    private int startSeason;
    private boolean seasonIsYear;

    public SeasonViewPagerAdapter(FragmentManager fragmentManager, int seasons, int startSeason, boolean seasonIsYear) {
        super(fragmentManager);
        this.seasons = seasons;
        this.startSeason = startSeason;
        this.seasonIsYear = seasonIsYear;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentSeason.newInstance(position + startSeason);
    }

    @Override
    public int getCount() {
        return seasons;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (seasonIsYear)
            return String.valueOf(position + startSeason);
        return "Season " + String.valueOf(position + startSeason);
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
