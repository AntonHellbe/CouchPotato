package se.mah.couchpotato.activitytvshow;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import se.mah.couchpotato.AllEpisodesListener;
import se.mah.couchpotato.CommunicationService;
import se.mah.couchpotato.EpisodeObject;
import se.mah.couchpotato.PosterListener;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class TvShowController implements AllEpisodesListener {

    private ActivityTvShow activity;
    private CommunicationService communicationService;
    private TvShowDataFragment dataFragment;
    private ServiceConnection serviceConnection;

    public TvShowController(ActivityTvShow activity, String tvShowId) {
        this.activity = activity;
        initializeDataFragment();
        dataFragment.setTvShowId(tvShowId);
        initializeCommunication();
        populateViewPager();
    }

    private void populateViewPager() {
        if (dataFragment.getEpisodes() != null)
            activity.updateData(dataFragment.getSeasons(), dataFragment.getStartSeason(), dataFragment.isSeasonIsYear());
    }

    private void initializeDataFragment() {
        FragmentManager fm = activity.getFragmentManager();
        dataFragment = (TvShowDataFragment) fm.findFragmentByTag("tvData");
        if(dataFragment == null){
            dataFragment = new TvShowDataFragment();
            fm.beginTransaction().add(dataFragment, "tvData").commit();
        }
    }

    private void initializeCommunication() {
        Intent intent = new Intent(activity, CommunicationService.class);
        serviceConnection = new ServiceConnection();
        activity.bindService(intent, serviceConnection,0);
    }

    public void getEpisodes() {
        if (dataFragment.getEpisodes() == null)
            communicationService.getAllEpisodes(dataFragment.getTvShowId(), this);
    }

    @Override
    public void onEpisodesRetrieved(ArrayList<EpisodeObject> shows) {
        Log.v("TvShowController", "EPISODES GOTTEN, size: " + shows.size());
        dataFragment.setEpisodes(shows);
        int seasons = 1;
        if (!shows.isEmpty()) {
            seasons = Math.max(shows.get(shows.size() - 1).getSeason() - shows.get(0).getSeason(), 1);  //atleast one season
            dataFragment.setStartSeason(shows.get(0).getSeason());
        }
        dataFragment.setSeasons(seasons);
        if (dataFragment.getStartSeason() > 1900)
            dataFragment.setSeasonIsYear(true);
        activity.updateData(dataFragment.getSeasons(), dataFragment.getStartSeason(), dataFragment.isSeasonIsYear());
    }
    
    public ArrayList<EpisodeObject> getEpisodesForSeason(int season) {
        ArrayList<EpisodeObject> episodesForSeason = new ArrayList<>();
        for (int i = 0; i < dataFragment.getEpisodes().size(); i++) {
            if (dataFragment.getEpisodes().get(i).getSeason() == season) {
                episodesForSeason.add(dataFragment.getEpisodes().get(i));
            }
        }
        return episodesForSeason;
    }

    public void downloadPoster(String url, String id, PosterListener listener) {
        if (communicationService != null)
            communicationService.downloadPicture(id, listener, url);
    }

    public TvShowDataFragment getDataFragment() {
        return dataFragment;
    }

    public void onPause() {
        if(activity.isFinishing())
            activity.getFragmentManager().beginTransaction().remove(dataFragment).commit();
    }

    public void onDestroy() {
        if (dataFragment.isBound()) {
            activity.unbindService(serviceConnection);
            if (dataFragment != null)
                dataFragment.setBound(false);
        }
    }


    private class ServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.LocalService ls = (CommunicationService.LocalService) service;
            communicationService = ls.getService(activity);
            Log.d("Controller","In onServiceConnected");
            dataFragment.setBound(true);
            getEpisodes();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataFragment.setBound(false);
        }
    }

}
