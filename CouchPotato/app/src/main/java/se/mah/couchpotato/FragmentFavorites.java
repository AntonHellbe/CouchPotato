package se.mah.couchpotato;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentFavorites extends Fragment implements FragmentInterface {

    private RecyclerView recyclerViewShows;
    private RecyclerViewAdapter adapter;
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        initalizeComponents(rootView);
        return rootView;
    }

    private void initalizeComponents(View rootView) {
        recyclerViewShows = (RecyclerView) rootView.findViewById(R.id.rv_favorites);
        recyclerViewShows.addItemDecoration(new RecyclerViewStaggeredSpacing(10));
        recyclerViewShows.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerViewAdapter();
        recyclerViewShows.setAdapter(adapter);

        

        AnimationSet set = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 100, 0);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController animationController = new LayoutAnimationController(set, R.integer.animationTime);

        recyclerViewShows.setLayoutAnimation(animationController);



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
    public void onResume() {
        super.onResume();
        getController().favoritesReceived(new ArrayList<TvShow>());  //TODO remove this test
    }
}
