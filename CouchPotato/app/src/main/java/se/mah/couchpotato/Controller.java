package se.mah.couchpotato;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import se.mah.couchpotato.activtysettings.ActivitySettings;

/**
 * Created by robin on 19/10/2017.
 */

public class Controller {

    private MainActivity mainActivity;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection;
    private DataFragment dataFragment;
    private SharedPreferences sP;
    private SharedPreferences.Editor editor;

    private boolean bound;
    private int showId;
    private ContainerFragment containerFragment;
    private String[] filters;


    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initializeResources();
        initializeDataFragment();
        initializeCommunication();
        sP = mainActivity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
    }

    private void initializeDataFragment() {
        FragmentManager fm = mainActivity.getFragmentManager();
        dataFragment = (DataFragment) fm.findFragmentByTag("data");
        if(dataFragment == null){
            dataFragment = new DataFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            fillFilter();
        }
    }

    private void fillFilter() {
        for (String category: filters)
            dataFragment.getFilterIncludeMap().put(category, true);
    }

    private void initializeCommunication() {
        Intent intent = new Intent(mainActivity,CommunicationService.class);

        if (!dataFragment.getServiceExist()){
            mainActivity.startService(intent);
            dataFragment.setServiceExist(true);
        }
        serviceConnection = new ServiceConnection();
        boolean status = mainActivity.bindService(intent, serviceConnection,0);
        Log.d("Controller","initializeCommunication, connected: " + status);
    }

    private void initializeResources() {
        filters = mainActivity.getResources().getStringArray(R.array.categories);
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }

    public void onPause() {
        if(mainActivity.isFinishing()){
            mainActivity.getFragmentManager().beginTransaction().remove(dataFragment).commit();
        }
    }

    public void onDestroy(){
        if(bound){
            if(mainActivity.isFinishing()){
                communicationService.stopService(new Intent(mainActivity, CommunicationService.class));
            }
            mainActivity.unbindService(serviceConnection);
            bound = false;
        }
    }

    public void onResume() {
        if (dataFragment.getFavorites() == null){
            HashMap<String, Integer> restoredFavourites = new HashMap<>();
            Map<String, ?> map = sP.getAll();
            for(Map.Entry<String, ?> entry: map.entrySet()){
                restoredFavourites.put(entry.getKey(), (Integer) entry.getValue());
            }
            //TODO call add favorite for every id
        }
    }

    //TODO kan också vara en string id, vilket som är smidigast
    public void addFavourite(TvShow show) {
        Log.d("CONTROLLERFAVORITE", show.getName() + " " + show.getUrl());
        HashMap<String,TvShow> favourites = dataFragment.getFavorites();
        favourites.put(""+show.getId(),show);
        editor = sP.edit();
        editor.putInt("" + show.getId(),show.getId());
        editor.commit();
        dataFragment.setFavorites(favourites);
    }

    public void removeFavourite(TvShow show){
        HashMap<String, TvShow> favourties = dataFragment.getFavorites();
        favourties.remove("" + show.getId());
        editor = sP.edit();
        editor.remove(""+show.getId());
        editor.commit();
        dataFragment.setFavorites(favourties);
    }

    public void scheduleRecieved(ArrayList<TvShow> shows) {
        for (int i = 0; i < shows.size(); i++) {
            Log.d("CONTROLLERSCHEDULE", shows.get(i).getShow().getName() + " " + shows.get(i).getShow().getUrl() + shows.get(i).getShow().getStatus());
        }
        dataFragment.setSchedule(shows);
        dataFragment.filterTvShows(shows);
        FragmentInterface feed = getFragmentByTag(ContainerFragment.TAG_FEED);
        feed.updateFragmentData(dataFragment.getSchedule());
    }

    public void favoritesReceived(TvShow show) {
        dataFragment.getFavorites().put(show.getId().toString(), show);
        FragmentInterface favorites = getFragmentByTag(ContainerFragment.TAG_FAVORITES);
        ArrayList<TvShow> tvshows = new ArrayList<>(dataFragment.getFavorites().values());
        favorites.updateFragmentData(tvshows);
    }

    public void searchReceived(ArrayList<TvShow> shows) {
        for(TvShow t: shows){
            Log.d("CONTROLLERSEARCH", t.getShow().getName() + " " + t.getShow().getRuntime() + " " + t.getShow().getUrl() + " " + t.getShow().getId());
        }
        FragmentInterface search = getFragmentByTag(ContainerFragment.TAG_SEARCH);
        dataFragment.setSearchResult(shows);
        search.updateFragmentData(shows);
    }

    public void downloadPoster(String url, String id, PosterListener posterListener) {
        if (communicationService != null)
            communicationService.downloadPicture(id, posterListener, url);
        else {
            dataFragment.getDownloadQueue().add(new DownloadImageRequest(url, id, posterListener));
        }
    }

    public void episodesRecieved(ArrayList<TvShow> episodeList){
        // @TODO - Do something with the recieved episodes
    }

    public void search(String searchString) {
        if (communicationService != null)
            communicationService.sendSearchTask(searchString);
        else {
            SearchQueryObject searchQuery = new SearchQueryObject(searchString);
            dataFragment.getDownloadQueue().add(searchQuery);
        }
    }

    private void sendInitialRequests() {
        if (dataFragment.getSchedule() == null)
            communicationService.sendSchedule();
        while (!dataFragment.getDownloadQueue().isEmpty()) {
            DownloadRequest downloadRequest = dataFragment.getDownloadQueue().pop();
            if (downloadRequest instanceof SearchQueryObject)
                communicationService.sendSearchTask(downloadRequest.getQuery());
            else if (downloadRequest instanceof DownloadImageRequest)
                communicationService.downloadPicture(((DownloadImageRequest) downloadRequest).getId(), ((DownloadImageRequest) downloadRequest).getPosterListener(), ((DownloadImageRequest) downloadRequest).getQuery());
        }
    }

    public void onSearchTextChanged(String searchString) {
        dataFragment.setSearchResult(null);
        if (searchString.length() > 3) {
            search(searchString);
        } else {
            FragmentInterface search = getFragmentByTag(ContainerFragment.TAG_SEARCH);
            search.updateFragmentData(new ArrayList<TvShow>());
        }
    }

    public void fabSettingsClicked(float x, float y) {
        Intent i = new Intent(mainActivity, ActivitySettings.class);
        i.putExtra("revealX", (int) x);    //to get middle of button
        i.putExtra("revealY", (int) y);
        i.putExtra("startRadius", 40);
        mainActivity.startActivityForResult(i, MainActivity.REQUESTCODESETTINGS);
    }

    public void fabFilterClicked() {
        mainActivity.toggleFilter();
    }

    public boolean navigationClicked(MenuItem item) {
        if (dataFragment.isAllowNavigation()) {
            mainActivity.hidekeyboard();
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    containerFragment.show(ContainerFragment.TAG_FEED);
                    return true;
                case R.id.navigation_favorites:
                    containerFragment.show(ContainerFragment.TAG_FAVORITES);
                    return true;
                case R.id.navigation_search:
                    containerFragment.show(ContainerFragment.TAG_SEARCH);
                    return true;
            }
        }
        return false;
    }

    public void fragmentHandling() {
        FragmentManager fml = mainActivity.getFragmentManager();
        containerFragment = (ContainerFragment) fml.findFragmentById(R.id.container_fragment);
        FragmentManager fm = containerFragment.getChildFragmentManager();
        if (!dataFragment.isFragmentInstantiated()) {
            containerFragment.add(new FragmentFavorites(), ContainerFragment.TAG_FAVORITES);
            containerFragment.add(new FragmentFeed(), ContainerFragment.TAG_FEED);
            containerFragment.add(new FragmentSearch(), ContainerFragment.TAG_SEARCH);
            containerFragment.setCurrentTag(ContainerFragment.TAG_FEED);
            dataFragment.setFragmentInstantiated(true);
        } else {
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_FEED),ContainerFragment.TAG_FEED);
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_FAVORITES),ContainerFragment.TAG_FAVORITES);
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_SEARCH),ContainerFragment.TAG_SEARCH);
        }
    }

    public FragmentInterface getFragmentByTag(String tag) {
        FragmentManager fm = containerFragment.getChildFragmentManager();
        return (FragmentInterface) fm.findFragmentByTag(tag);
    }

    public void modifyFilter(String category, boolean include) {
        dataFragment.getFilterIncludeMap().put(category, include);
        FragmentInterface feed = getFragmentByTag(ContainerFragment.TAG_FEED);
        FragmentInterface favorites = getFragmentByTag(ContainerFragment.TAG_FAVORITES);
        FragmentInterface search = getFragmentByTag(ContainerFragment.TAG_SEARCH);

        //TODO update the recyclerviews
    }

    public String[] getFilters() {
        return filters;
    }

    private class ServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.LocalService ls = (CommunicationService.LocalService) service;
            communicationService = ls.getService(mainActivity);
            Log.d("Controller","In onServiceConnected");
            bound = true;
            sendInitialRequests();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    }

}
