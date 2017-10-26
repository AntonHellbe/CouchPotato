package se.mah.couchpotato;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentFavorites extends Fragment implements FragmentInterface {

    private RecyclerView recyclerViewShows;
    private RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        initalizeComponents(rootView);
        return rootView;
    }

    private void initalizeComponents(View rootView) {
        recyclerViewShows = (RecyclerView) rootView.findViewById(R.id.rv_favorites);
        int columns = getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE ? 4 : 2;
        recyclerViewShows.addItemDecoration(new RecyclerViewStaggeredSpacing(10, columns));
        recyclerViewShows.setLayoutManager(new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL));
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

    @Override
    public void insertTvShow(TvShow show) {
        adapter.insertTvShow(show);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setTvShowArrayList(getController().getDataFragment().filterShows(new ArrayList<>(getController().getDataFragment().getFavorites().values())));
    }
}
