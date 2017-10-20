package se.mah.couchpotato;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *@author Jonatan Fridsten
 */

public class ContainerFragment extends Fragment{

    private FragmentViewer viewer;
    public static final String TAG_FAVORITES = "FAVORITES";
    public static final String TAG_FEED = "FEED";
    public static final String TAG_SEARCH = "SEARCH";

    public ContainerFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v("ContainerFragment","Hello ? ");
        View view = inflater.inflate(R.layout.fragment_container,container,false);
        FragmentManager fm = getChildFragmentManager();
        viewer = new FragmentViewer((MainActivity) getActivity(), fm, R.id.fragment_container);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewer.show();
    }

    public void add(Fragment fragment,String tag){
        viewer.add(fragment,tag);
    }

    public void setCurrentTag(String tag){
        viewer.setCurrentTag(tag);
    }

    public String getCurrentTag(){
        return viewer.getCurrentTag();
    }

    public String show(){
        return viewer.show();
    }

    public String show(String tag){
        return viewer.show(tag);
    }
}
