package se.mah.couchpotato;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initializeDataFragment();
        initializeCommunication();
        sP = mainActivity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        editor = sP.edit();
    }

    private void initializeDataFragment() {
        FragmentManager fm = mainActivity.getFragmentManager();
        dataFragment = (DataFragment) fm.findFragmentByTag("data");
        if(dataFragment == null){
            dataFragment = new DataFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
        }
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


    public void sendTest() {
        //communicationService.setController(this);
        communicationService.sendSearchTask("girls");
        communicationService.sendAddFavorite("1");
        communicationService.sendSchedule();
    }

    //TODO kan också vara en string id, vilket som är smidigast
    public void addFavourite(TvShow show) {
        Log.d("CONTROLLERFAVORITE", show.getName() + " " + show.getUrl());
        //add tvshow to favorites or retrieve all favorites again (solves the update situation)
        //optional: update favorites

        //Retrieve all current favourites
        HashMap<String,TvShow> favourites = dataFragment.getFavorites();
        //put the new favourite in the hashmap
        favourites.put(""+show.getId(),show);
        //Put it in the SharedPreference
        editor.putInt("" + show.getId(),show.getId());
        //Update the favourite list
        dataFragment.setFavorites(favourites);
    }

    public void removeFacourite(TvShow show){
        //Retrieve all favourites
        HashMap<String, TvShow> favourties = dataFragment.getFavorites();
        //Remove the show from the hashmap
        favourties.remove("" + show.getId());
        //Remove it from the SharedPreference
        editor.remove(""+show.getId());
        //Update the favourite list
        dataFragment.setFavorites(favourties);
    }

    public void scheduleRecieved(ArrayList<TvShow> shows) {
        for (int i = 0; i < shows.size(); i++) {
            Log.d("CONTROLLERSCHEDULE", shows.get(i).getShow().getName() + " " + shows.get(i).getShow().getUrl() + shows.get(i).getShow().getStatus());
        }
        dataFragment.setSchedule(shows);
        FragmentInterface feed = mainActivity.getFragmentByTag(ContainerFragment.TAG_FEED);
        feed.updateFragmentData(dataFragment.getSchedule(), false);
    }

    public void favoritesReceived(TvShow show) {
        dataFragment.getFavorites().put(show.getId().toString(), show);
        FragmentInterface favorites = mainActivity.getFragmentByTag(ContainerFragment.TAG_FAVORITES);
        ArrayList<TvShow> tvshows = new ArrayList<>(dataFragment.getFavorites().values());
        favorites.updateFragmentData(tvshows, false);
    }

    public void searchReceived(ArrayList<TvShow> shows) {
        for(TvShow t: shows){
            Log.d("CONTROLLERSEARCH", t.getShow().getName() + " " + t.getShow().getRuntime() + " " + t.getShow().getUrl() + " " + t.getShow().getId());
        }
        FragmentInterface search = mainActivity.getFragmentByTag(ContainerFragment.TAG_SEARCH);
        search.updateFragmentData(shows, false);
    }

    public void imageReceived() {
        FragmentInterface feed = mainActivity.getFragmentByTag(ContainerFragment.TAG_FEED);
        FragmentInterface favorites = mainActivity.getFragmentByTag(ContainerFragment.TAG_FAVORITES);
        FragmentInterface search = mainActivity.getFragmentByTag(ContainerFragment.TAG_SEARCH);
        feed.updateFragmentData(dataFragment.getSchedule(), true);
//        favorites.updateFragmentData(new ArrayList<>(dataFragment.getFavorites().values()));  //null
    }

    public void search(String searchString) {
        if (communicationService != null)
            communicationService.sendSearchTask(searchString);
        else
            dataFragment.getSearhQueue().add(searchString);
    }

    private void sendInitialRequests() {
        if (dataFragment.getSchedule() == null)
            communicationService.sendSchedule();
        if (!dataFragment.getSearhQueue().isEmpty()) {
            String searchString = dataFragment.getSearhQueue().pop();
            communicationService.sendSearchTask(searchString);
        }
//        if (dataFragment.getFavorites() == null)
//            communicationService.sendAddFavorite("");
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
