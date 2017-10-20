package se.mah.couchpotato;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by robin on 19/10/2017.
 */

public class Controller {

    private MainActivity mainActivity;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection;
    private DataFragment dataFragment;
    private SharedPreferences sP;

    private boolean bound;
    private int showId;


    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initializeDataFragment();
        initializeCommunication();
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

    public void onPause() {
        sP = mainActivity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putInt("showId",0);
        editor.apply();
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
        sP = mainActivity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        showId = sP.getInt("showId",0);
    }


    public void sendTest() {
        //communicationService.setController(this);
        communicationService.sendToURL("test");
    }

    //TODO kan också vara en string id, vilket som är smidigast
    public void favoriteAdded(TvShow show) {
        //add tvshow to favorites or retrieve all favorites again (solves the update situation)
        //optional: update favorites
    }

    public void scheduleRecieved(ArrayList<TvShow> shows) {
        dataFragment.setSchedule(shows);
        FragmentInterface feed = mainActivity.getFragmentByTag(ContainerFragment.TAG_FEED);
        feed.updateFragmentData(dataFragment.getSchedule());
    }

    public void favoritesReceived(ArrayList<TvShow> shows) {    //TODO should this be ArrayList?
        FragmentInterface favorites = mainActivity.getFragmentByTag(ContainerFragment.TAG_FAVORITES);
        favorites.updateFragmentData(shows);
    }

    public void searchReceived(ArrayList<TvShow> shows) {
        FragmentInterface search = mainActivity.getFragmentByTag(ContainerFragment.TAG_SEARCH);
        search.updateFragmentData(shows);
    }

    public void recievedData(TvShow tvShow) {
        Log.d("Controller","In recivedData, the jsonObject contains: " + tvShow.toString());
    }

    public void search(String searchString) {
        //TODO send search request
    }

    public void retrieveFavorites() {
        if (dataFragment.getFavorites() == null) {
            //TODO communicationservice get favorites
        }
    }

    public void retrieveFeed() {
        if (dataFragment.getSchedule() == null) {
            //TODO communicationservice get feed
        }
    }

    private class ServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.LocalService ls = (CommunicationService.LocalService) service;
            communicationService = ls.getService(mainActivity);
            Log.d("Controller","In onServiceConnected");
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    }

}
