package se.mah.couchpotato;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import se.mah.couchpotato.activtysettings.ActivitySettings;
import se.mah.couchpotato.activtysettings.Settings;

/**
 * Created by robin on 19/10/2017.
 */

public class Controller {

    private MainActivity mainActivity;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection;
    //private AlarmServiceConnection alarmServiceConnection;
    private NotificationAlarmService notify;
    private DataFragment dataFragment;
    private SharedPreferences sP;
    private SharedPreferences.Editor editor;

    private boolean bound,alarmBound;
    private int showId;
    private ContainerFragment containerFragment;
    private String[] filters;




    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        sP = mainActivity.getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        initializeResources();
        initializeDataFragment();
        if (!dataFragment.isFavoritesHandled())
            restoreFavourites();
        if (dataFragment.getSettings() == null)
            restoreSettings();
        initializeCommunication();
        alarm();
        //initializeAlarm();
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

    public void initializeAlarm(){
        Intent intent = new Intent(mainActivity, NotificationAlarmService.class);

        if(!dataFragment.getAlarmExist()){
            mainActivity.startService(intent);
            dataFragment.setAlarmExist(true);
        }
        //alarmServiceConnection = new AlarmServiceConnection();
        //boolean status = mainActivity.bindService(intent, alarmServiceConnection, 0);
        //Log.d("NOTIFICATIONTEST", "initializeAlarm, connected " + status);
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }

    public void onPause() {
        Log.d("ControllerSettings","in onPause");
        saveFavourites();
        saveSettingsToSP();
        if(mainActivity.isFinishing()){
            Log.d("ControllerSettings","in onPause, is finishing");
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

    public void alarm(){
        Log.d("NOTIFICATIONTEST","in alarm");


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()+1000*60);
        //calendar.set(Calendar.HOUR_OF_DAY, 10);
        //calendar.set(Calendar.MINUTE, 4);

        AlarmManager manager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mainActivity, NotificationReciever.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity,0,intent,0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*1000,pendingIntent);

    }
    public void notification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mainActivity)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle("My notification")
                        .setContentText(mainActivity.getResources().getString(R.string.notification_description));
        Intent resultIntent = new Intent(mainActivity, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mainActivity,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        int id = 001;
        NotificationManager notifyManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.notify(id,mBuilder.build());
    }

    private void restoreFavourites() {
        Log.d("ControllerFavourites","in restoreFavourites");
        if (sP.contains("favourites")) {
            ArrayList<String> favIds = new ArrayList<>(sP.getStringSet("favourites", null));
            FavoriteListenerCallback callback = new FavoriteListenerCallback();
            Log.d("ControllerFavourites","in restoreFavourites, inside if ");
            for (int i = 0; i < favIds.size(); i++) {
                DownloadFavoriteRequest req = new DownloadFavoriteRequest(favIds.get(i), callback);
                dataFragment.getDownloadQueue().add(req);
                Log.d("ControllerFavourites","in restoreFavourites, the restored number: " + favIds.get(i));
            }
        }
        dataFragment.setFavoritesHandled(true);
    }

    private void saveFavourites() {
        Log.d("ControllerFavourites","in saveFavorites");
        if (dataFragment.getFavorites() != null) {
            HashMap<String, TvShow> favourites = dataFragment.getFavorites();
            Log.d("ControllerFavourites", "in saveFavorites, size: " + favourites.size());
            editor = sP.edit();
            editor.putStringSet("favourites", favourites.keySet());
            editor.apply();
        }
        Log.d("ControllerFavourites","exiting saveFavorites");
    }

    public void scheduleRecieved(ArrayList<TvShow> shows) {
        for (int i = 0; i < shows.size(); i++) {
            Log.d("CONTROLLERSCHEDULE", shows.get(i).getShow().getName() + " " + shows.get(i).getShow().getUrl() + shows.get(i).getShow().getStatus());
        }
        dataFragment.setSchedule(shows);
        dataFragment.filterTvShows(shows);
        ArrayList<TvShow> filteredFeed = dataFragment.filterShows(shows);
        FragmentInterface feed = getFragmentByTag(ContainerFragment.TAG_FEED);
        feed.updateFragmentData(filteredFeed);
    }

    public void searchReceived(ArrayList<TvShow> shows) {
        for(TvShow t: shows){
            Log.d("CONTROLLERSEARCH", t.getShow().getName() + " " + t.getShow().getRuntime() + " " + t.getShow().getUrl() + " " + t.getShow().getId());
        }
        dataFragment.setSearchResult(shows);
        ArrayList<TvShow> filteredResult = dataFragment.filterShows(dataFragment.getSearchResult());
        FragmentInterface search = getFragmentByTag(ContainerFragment.TAG_SEARCH);
        search.updateFragmentData(filteredResult);
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
            else if (downloadRequest instanceof DownloadFavoriteRequest)
                communicationService.sendGetFavorite(downloadRequest.getQuery(), ((DownloadFavoriteRequest) downloadRequest).getCallback());
        }
    }

    public void onSearchTextChanged(String searchString) {
//        dataFragment.setSearchResult(new ArrayList<TvShow>());
        if (searchString.length() > 3) {
            search(searchString);
        } else {
            FragmentInterface search = getFragmentByTag(ContainerFragment.TAG_SEARCH);
            search.updateFragmentData(new ArrayList<TvShow>());
        }
    }

    public void saveSettings(Bundle bundle){

        // TODO: 25/10/2017 change the current settings to the newly changed, and also save it into sP
        Settings settings = bundle.getParcelable(ActivitySettings.SETTINGS_BUNDLE_NAME);

        //Checks if language have been changed or nah
        if(!dataFragment.getSettings().getLanguage().equals(settings.getLanguage())){
            mainActivity.recreate();
        }

        //First convert settings to strings/int/other things that can be saved in sP
        //Then remove old settings from sP and replace them with the new ones
        //After that save it to datafragment
        dataFragment.setSettings(settings);
    }

    private void saveSettingsToSP(){

        editor = sP.edit();
        editor.putBoolean(Settings.NSFW,dataFragment.getSettings().isNsfw());
        editor.putString(Settings.LANG, dataFragment.getSettings().getLanguage());
        editor.putString(Settings.COUNT, dataFragment.getSettings().getCountry());
        editor.putInt(Settings.POSLANG, dataFragment.getSettings().getPosition_lang());
        editor.putInt(Settings.POSCOUNT, dataFragment.getSettings().getPosition_count());
        editor.apply();
        Log.d("ControllerSettings", "in saveSettingsToSP, saved stuff");
    }

    public void restoreSettings(){
        if (!sP.contains("language")){
            Settings settings = new Settings();
            dataFragment.setSettings(settings);
            Log.d("ControllerSettings","in restoreSettings, does not contain the word settings");
        }else {
            Settings settings = new Settings(
                    sP.getBoolean(Settings.NSFW, true),
                    sP.getString(Settings.LANG,null),
                    sP.getString(Settings.COUNT,null),
                    sP.getInt(Settings.POSLANG,0),
                    sP.getInt(Settings.POSCOUNT,0));
            dataFragment.setSettings(settings);
        }
    }

    public void fabSettingsClicked(float x, float y) {
        Intent i = new Intent(mainActivity, ActivitySettings.class);
        i.putExtra("revealX", (int) x);    //to get middle of button
        i.putExtra("revealY", (int) y);
        i.putExtra("startRadius", 40);
//        @TODO Använd data fragment istället
//        Settings settings = new Settings();
        Bundle bundle = new Bundle();
//        bundle.putParcelable("data_settings",settings);
        Settings settings = dataFragment.getSettings();
        bundle.putParcelable(ActivitySettings.SETTINGS_BUNDLE_NAME,settings);
        i.putExtras(bundle);
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
        feed.updateFragmentData(dataFragment.filterShows(dataFragment.getSchedule()));
        search.updateFragmentData(dataFragment.filterShows(dataFragment.getSearchResult()));
    }

    public String[] getFilters() {
        return filters;
    }

    public void modifyFavorites(String id, boolean favorite) {
        if (favorite) {
            if (!dataFragment.getFavorites().containsKey(id)) {
                communicationService.sendGetFavorite(id, new FavoriteListenerCallback());
            }
        }
        else {
            if (dataFragment.getFavorites().containsKey(id)) {
                dataFragment.getFavorites().remove(id);
            }
        }
    }

    public void networkChange(NetworkInfo networkInfo) {
        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
            if(communicationService != null) {
                communicationService.executeCommands();
            }
        }
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

    private class FavoriteListenerCallback implements FavoriteListener{

        @Override
        public void onFavoriteRecieved(TvShow tvShow) {
            Log.v("RECIEVEDFAV", "ID: " + tvShow.getName() + tvShow.getId());
            FragmentInterface favorites = getFragmentByTag(ContainerFragment.TAG_FAVORITES);
            dataFragment.getFavorites().put(tvShow.getShow().getId().toString(), tvShow);
            favorites.insertTvShow(tvShow);
        }
    }

    /*private class AlarmServiceConnection implements android.content.ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NotificationAlarmService.LocalService ls = (NotificationAlarmService.LocalService) service;
            notify = ls.getService(mainActivity);
            Log.d("Controller","In onServiceConnected");
            alarmBound = true;
            notify.startAlarm();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            alarmBound = false;
        }
    }*/
}
