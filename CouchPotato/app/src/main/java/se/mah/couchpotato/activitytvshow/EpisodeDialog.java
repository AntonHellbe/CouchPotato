package se.mah.couchpotato.activitytvshow;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.mah.couchpotato.R;

/**
 * Created by Gustaf Bohlin on 23/10/2017.
 */

public class EpisodeDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_episode, container, false);
        return rootView;
    }
}
