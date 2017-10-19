package se.mah.couchpotato;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public interface FragmentInterface {

    Controller getController();

    void updateFragmentData(ArrayList<TvShow> shows);
}
