package se.mah.couchpotato;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentSearch extends Fragment implements FragmentInterface {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    @Override
    public void getController() {
        //((ActivityInterface) getActivity()).getController();
    }

    @Override
    public void updateFragmentData() {

    }
}
