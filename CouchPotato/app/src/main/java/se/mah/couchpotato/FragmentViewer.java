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
    private String currentTag;

    public FragmentViewer(FragmentManager fragmentManager, int container) {
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
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentTag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!tag.equals(currentTag)) {
                int[] data = getAnimator(tag);
                fragmentTransaction.setCustomAnimations(data[0],data[1]);
            }
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
            currentTag = tag;
        }
        return currentTag;
    }

    public String show() {
        return show(currentTag);
    }

    public String getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }

    private int[] getAnimator(String tag) {
        int[] data = new int[2];
        switch (tag) {
            case ContainerFragment.TAG_FAVORITES:
                if (currentTag.equals(ContainerFragment.TAG_FEED)) {
                    data[0] = R.animator.slide_in_right;
                    data[1] = R.animator.slide_out_left;
                    //data[0] = R.anim.anim_slide_in_right;
                    //data[1] = R.anim.anim_slide_out_left;
                } else {
                    data[0] = R.animator.slide_in_left;
                    data[1] = R.animator.slide_out_right;
                    //data[0] = R.anim.anim_slide_in_right;
                    //data[1] = R.anim.anim_slide_out_left;
                }
                return data;
            case ContainerFragment.TAG_FEED:
                data[0] = R.animator.slide_in_left;
                data[1] = R.animator.slide_out_left;
                return data;
            case ContainerFragment.TAG_SEARCH:
                data[0] = R.animator.slide_in_right;
                data[1] = R.animator.slide_out_right;
                return data;
            default:
                break;
        }
        return null;
    }
}
