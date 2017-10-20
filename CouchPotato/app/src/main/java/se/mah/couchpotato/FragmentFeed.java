package se.mah.couchpotato;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
        initializeComponents(rootView);
        return rootView;
    }

    private void initializeComponents(View rootView) {
        recyclerViewShows = (RecyclerView) rootView.findViewById(R.id.rv_feed);
        recyclerViewShows.addItemDecoration(new RecyclerViewStaggeredSpacing(10));
        recyclerViewShows.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerViewAdapter((AppCompatActivity) getActivity());
        recyclerViewShows.setAdapter(adapter);
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
