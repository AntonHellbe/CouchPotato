package se.mah.couchpotato;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
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
    private boolean bound, connected;

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
        if (communicationService != null){communicationService.setController(this);}
        else Log.d("Controller","CS is NULL!!!!!!!");
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

    public void setServiceController(){
        communicationService.setController(this);
    }

    public void sendTest() {
        //communicationService.setController(this);
        communicationService.sendToURL("test");
    }

    public void scheduleRecieved(Schedule schedule) {
        //add schedule to datafragment
        //update data in feed
    }

    public void favoritesReceived(ArrayList<TvShow> shows) {
        //add favorites to datafragment
        //update data in favorites
    }

    public void searchReceived(ArrayList<TvShow> shows) {
        //update data in search
    }

    public void recievedData(TvShow tvShow) {
        Log.d("Controller","In recivedData, the jsonObject contains: " + tvShow.toString());
    }

    private class ServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.LocalService ls = (CommunicationService.LocalService) service;
            communicationService = ls.getService();
            setServiceController();
            Log.d("Controller","In onServiceConnected");
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    }

}
