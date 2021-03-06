package se.mah.couchpotato.activitytvshow;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import se.mah.couchpotato.EpisodeObject;
import se.mah.couchpotato.R;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentSeason extends Fragment {

    private int season;
    private TextView tvNoEpisodes;

    static FragmentSeason newInstance(int season) {
        FragmentSeason fragment = new FragmentSeason();
        Bundle args = new Bundle();
        args.putInt("season", season);
        Log.v("FRAGMENTSEASON", season + " ");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        season = getArguments() != null ? getArguments().getInt("season") : 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_season, container, false);
        initializeComponent(rootView);
        Log.v("FRAGMENTSEASON", "Opened season " + String.valueOf(season));
        return rootView;
    }

    private void initializeComponent(View rootView) {
        RecyclerView rvSeason = (RecyclerView) rootView.findViewById(R.id.rv_episodes);
        tvNoEpisodes = (TextView) rootView.findViewById(R.id.tv_no_episodes);
        ArrayList<EpisodeObject> episodes = ((ActivityTvShow) getActivity()).getController().getEpisodesForSeason(season);
        RecyclerViewSeasonAdapter adapter = new RecyclerViewSeasonAdapter(episodes, (ActivityTvShow) getActivity());
        rvSeason.addItemDecoration(new RecyclerViewSpacing(10));
        rvSeason.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSeason.setAdapter(adapter);
        if (episodes.size() == 0)
            tvNoEpisodes.setVisibility(View.VISIBLE);
        else
            tvNoEpisodes.setVisibility(View.GONE);
    }
}
