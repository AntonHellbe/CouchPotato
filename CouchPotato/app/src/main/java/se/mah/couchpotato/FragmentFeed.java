package se.mah.couchpotato;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentFeed extends Fragment implements FragmentInterface {

    private Button testButton;
    private RecyclerView recyclerViewShows;
    private RecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        temporaryForTesting(rootView);
        initializeComponents(rootView);
        return rootView;
    }

    private void initializeComponents(View rootView) {
        recyclerViewShows = (RecyclerView) rootView.findViewById(R.id.rv_feed);
        recyclerViewShows.addItemDecoration(new RecyclerViewStaggeredSpacing(10));
        recyclerViewShows.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerViewAdapter();
        recyclerViewShows.setAdapter(adapter);
    }

    /**
     * Just for testing if the communicationService is working.
     * */
    private void temporaryForTesting(View rootView) {
        //final Controller controller = new Controller();
        testButton = (Button) rootView.findViewById(R.id.testknapp);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getController().sendTest();
            }
        });
        Button testButton2 = (Button) rootView.findViewById(R.id.testknapp2);

        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityTvShow.class);
                ((MainActivity) getActivity()).startActivity(i);
            }
        });
    }

    @Override
    public Controller getController() {
        return ((ActivityInterface) getActivity()).getController();
    }

    @Override
    public void updateFragmentData(ArrayList<TvShow> shows) {
        adapter.setTvShowArrayList(shows);
    }
}
