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

    public FragmentViewer(FragmentManager fragmentManager,int container) {
        this.fragmentManager = fragmentManager;
        this.container = container;
    }

    public void add(Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            fragmentTransaction.add(container,fragment,tag);
        }
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    public String show(String tag){
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentTag);
        if (fragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(!tag.equals(currentTag)){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_left);
            }
            if (currentFragment != null){
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

    private
}
