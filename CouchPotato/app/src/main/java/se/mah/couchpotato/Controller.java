package se.mah.couchpotato;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by robin on 19/10/2017.
 */

public class Controller {

    private MainActivity mainActivity;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection;
    private boolean bound, connected;
    /**
     * Temporary for testing
     * */


    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initializeCommunication();

    }

    private void initializeCommunication() {
        Intent intent = new Intent(mainActivity,CommunicationService.class);

        // TODO: 19/10/2017 check DataFragment if a connection is active

        serviceConnection = new ServiceConnection();
        boolean status = mainActivity.bindService(intent, serviceConnection,0);
        Log.d("Controller","initializeCommunication, connected: " + status);
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

    public void sendTest() {
        communicationService.sendToURL("test");
    }

    private class ServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.LocalService ls = (CommunicationService.LocalService) service;
            communicationService = ls.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    }

}
