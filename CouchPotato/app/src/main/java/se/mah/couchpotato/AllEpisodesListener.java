package se.mah.couchpotato;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public interface AllEpisodesListener {
    void onEpisodesRetrieved(ArrayList<TvShow> shows);
}
