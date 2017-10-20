package se.mah.couchpotato;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {
    private boolean serviceExist;
    private String currentTag;
    private ArrayList<TvShow> schedule;
    private HashMap<String, TvShow> favorites;
    private boolean allowNavigation = true;
    private ArrayDeque<String> searhQueue = new ArrayDeque<>();
    private HashMap<String, Bitmap> pictureMap = new HashMap<>();

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

    public ArrayDeque<String> getSearhQueue() {
        return searhQueue;
    }

    public void setSearhQueue(ArrayDeque<String> searhQueue) {
        this.searhQueue = searhQueue;
    }

    public void putPictureMap(String id, Bitmap bitmap){
        pictureMap.put(id, bitmap);
    }

    public Bitmap getPicture(String id){
        return pictureMap.get(id);
    }
}
