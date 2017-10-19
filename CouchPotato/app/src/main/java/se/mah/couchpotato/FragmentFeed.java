package se.mah.couchpotato;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentFeed extends Fragment implements FragmentInterface {

    private Button testButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        temporaryForTesting(rootView);

        return rootView;
    }

    /**
     * Just for testing if the communicationService is working.
     * */
    private void temporaryForTesting(View rootView) {
        final Controller controller = new Controller();
        testButton = (Button) rootView.findViewById(R.id.testknapp);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.sendTest();
            }
        });
    }

    @Override
    public void getController() {
        //((ActivityInterface) getActivity()).getController();
    }

    @Override
    public void updateFragmentData() {

    }
}
