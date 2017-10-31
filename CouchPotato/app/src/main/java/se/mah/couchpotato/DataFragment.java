package se.mah.couchpotato;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.mah.couchpotato.activtysettings.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {

    private static final String OTHER = "Other";
    private static final String ADULT = "Adult";

    private boolean serviceExist, alarm;
    private String currentTag;
    private ArrayList<TvShow> schedule;
    private ArrayList<TvShow> searchResult = new ArrayList<>();
    private HashMap<String, TvShow> favorites = new HashMap<>();
    private boolean allowNavigation = true;
    private ArrayDeque<DownloadRequest> downloadQueue = new ArrayDeque<>();
    private HashMap<String, Bitmap> pictureMap = new HashMap<>();
    private boolean fragmentInstantiated = false;
    private HashMap<String, Boolean> filterIncludeMap = new HashMap<>();
    private HashMap<String, ArrayList<TvShow>> filterMap = new HashMap();
    private Settings settings;
    private boolean favoritesHandled = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setServiceExist(boolean serviceExist) {
        this.serviceExist = serviceExist;
    }

    public boolean getServiceExist() {
        return serviceExist;
    }

    public String getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }

    public ArrayList<TvShow> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<TvShow> schedule) {
        this.schedule = schedule;
    }

    public HashMap<String, TvShow> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, TvShow> favorites) {
        this.favorites = favorites;
    }

    public boolean isAllowNavigation() {
        return allowNavigation;
    }

    public void setAllowNavigation(boolean allowNavigation) {
        this.allowNavigation = allowNavigation;
    }

    public void putPictureMap(String id, Bitmap bitmap){
        pictureMap.put(id, bitmap);
    }

    public Bitmap getPicture(String id){
        return pictureMap.get(id);
    }

    public ArrayList<TvShow> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<TvShow> searchResult) {
        this.searchResult = searchResult;
    }

    public ArrayDeque<DownloadRequest> getDownloadQueue() {
        return downloadQueue;
    }

    public void setDownloadQueue(ArrayDeque<DownloadRequest> downloadQueue) {
        this.downloadQueue = downloadQueue;
    }

    public boolean isFragmentInstantiated() {
        return fragmentInstantiated;
    }

    public void setFragmentInstantiated(boolean fragmentInstantiated) {
        this.fragmentInstantiated = fragmentInstantiated;
    }

    public HashMap<String, ArrayList<TvShow>> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(HashMap<String, ArrayList<TvShow>> filterMap) {
        this.filterMap = filterMap;
    }

    public void filterTvShows(ArrayList<TvShow> tvShowList){
        for(TvShow t: tvShowList){
            if(t.getShow().getGenres().isEmpty()){
                if(filterMap.get(OTHER) == null){
                    filterMap.put(OTHER, new ArrayList<TvShow>());
                }
                filterMap.get(OTHER).add(t);
            }else{
                boolean nudes = false;
                for (int i = 0; i < t.getShow().getGenres().size(); i++) {
                    if(t.getShow().getGenres().get(i).equals(ADULT)){
                        nudes = true;
                    }
                }
                if(!nudes) {
                    for (int i = 0; i < t.getShow().getGenres().size(); i++) {
                        if (filterMap.get(t.getShow().getGenres().get(i)) == null)
                            filterMap.put((String) t.getShow().getGenres().get(i), new ArrayList<TvShow>());

                        filterMap.get((String) t.getShow().getGenres().get(i)).add(t);
                    }
                }else{
                    if(filterMap.get(ADULT) == null)
                        filterMap.put(ADULT, new ArrayList<TvShow>());

                    filterMap.get(ADULT).add(t);
                }
            }

        }

    }

    public ArrayList<TvShow> filterShows(ArrayList<TvShow> tvShowFilter) {
        ArrayList<TvShow> filteredResult = new ArrayList<>();
        for (TvShow t : tvShowFilter) {
            if (t.getShow().getGenres().size() == 0) {
                if (filterIncludeMap.get(OTHER))
                        filteredResult.add(t);
                    } else {
                        for (int i = 0; i < t.getShow().getGenres().size(); i++) {
                            if (filterIncludeMap.get(t.getShow().getGenres().get(i))) {
                                if(!settings.isNsfw() && t.getShow().getGenres().get(i).equals(ADULT)){
                                    continue;
                                }
                                filteredResult.add(t);
                                break;
                            }
                        }
                    }

        }
        return filteredResult;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings(){
        return this.settings;
    }

    public HashMap<String, Boolean> getFilterIncludeMap() {
        return filterIncludeMap;
    }

    public void setFilterIncludeMap(HashMap<String, Boolean> filterIncludeMap) {
        this.filterIncludeMap = filterIncludeMap;
    }

    public boolean isFavoritesHandled() {
        return favoritesHandled;
    }

    public void setFavoritesHandled(boolean favoritesHandled) {
        this.favoritesHandled = favoritesHandled;
    }

    public boolean getAlarmExist() {
        return alarm;
    }

    public void setAlarmExist(boolean alarmExist) {
        this.alarm = alarmExist;
    }
}
