package se.mah.couchpotato.activitytvshow;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import se.mah.couchpotato.TvShow;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class TvShowDataFragment extends Fragment {

    private String tvShowId;
    private ArrayList<TvShow> episodes;
    private HashMap<String, Bitmap> pictureMap = new HashMap<>();
    private int seasons;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public String getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(String tvShowId) {
        this.tvShowId = tvShowId;
    }

    public ArrayList<TvShow> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<TvShow> episodes) {
        this.episodes = episodes;
    }

    public Bitmap getPicture(String id) {
        return pictureMap.get(id);
    }

    public void putPicture(String id, Bitmap bitmap) {
        pictureMap.put(id, bitmap);
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }
}
