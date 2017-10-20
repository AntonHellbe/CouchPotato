package se.mah.couchpotato;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * @author Jonatan Fridsten
 */

public class FragmentViewer {

    private int container;
    private FragmentManager fragmentManager;
    private MainActivity activity;
    public static final int ANIMATIONTIME = 200;    //TODO remember me?

    public FragmentViewer(MainActivity activity, FragmentManager fragmentManager, int container) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.container = container;
    }

    public void add(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(container, fragment, tag);
        }
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    public String show(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        Fragment currentFragment = fragmentManager.findFragmentByTag(getCurrentTag());
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!tag.equals(getCurrentTag())) {
                int[] data = getAnimator(tag);
                fragmentTransaction.setCustomAnimations(data[0],data[1]);
            }
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
            new TimingThread(ANIMATIONTIME).start();
            setCurrentTag(tag);
        }
        return getCurrentTag();
    }

    public String show() {
        return show(getCurrentTag());
    }

    private int[] getAnimator(String tag) {
        int[] data = new int[2];
        switch (tag) {
            case ContainerFragment.TAG_FAVORITES:
                if (getCurrentTag().equals(ContainerFragment.TAG_FEED)) {
                    data[0] = R.animator.slide_in_right;
                    data[1] = R.animator.slide_out_left;
                } else {
                    data[0] = R.animator.slide_in_left;
                    data[1] = R.animator.slide_out_right;
                }
                return data;
            case ContainerFragment.TAG_FEED:
                data[0] = R.animator.slide_in_left;
                data[1] = R.animator.slide_out_right;
                return data;
            case ContainerFragment.TAG_SEARCH:
                data[0] = R.animator.slide_in_right;
                data[1] = R.animator.slide_out_left;
                return data;
            default:
                break;
        }
        return null;
    }

    public String getCurrentTag(){
        return activity.getController().getDataFragment().getCurrentTag();
    }

    public void setCurrentTag(String currentTag){
        activity.getController().getDataFragment().setCurrentTag(currentTag);
    }

    private class ChangeAllowNavigation implements Runnable {

        private boolean navigation;

        public ChangeAllowNavigation(boolean navigation) {
            this.navigation = navigation;
        }

        @Override
        public void run() {
            activity.getController().getDataFragment().setAllowNavigation(navigation);
        }
    }

    private class TimingThread extends Thread {

        private long animationtime;
        public TimingThread(int animationtimeInt) {
            animationtime = (long) animationtimeInt;
        }

        @Override
        public void run() {
            super.run();
            try {
                activity.runOnUiThread(new ChangeAllowNavigation(false));
                Thread.sleep(animationtime);
                activity.runOnUiThread(new ChangeAllowNavigation(true));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
