package se.mah.couchpotato.activitytvshow;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import se.mah.couchpotato.EpisodeObject;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class TvShowDataFragment extends Fragment {

    private String tvShowId;
    private ArrayList<EpisodeObject> episodes;
    private HashMap<String, Bitmap> pictureMap = new HashMap<>();
    private int seasons;
    private int startSeason;
    private boolean seasonIsYear;
    private boolean bound = false;
    private boolean favorite;
    private Bitmap hdImage;
    private String hdImagePath;

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

    public ArrayList<EpisodeObject> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<EpisodeObject> episodes) {
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

    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public int getStartSeason() {
        return startSeason;
    }

    public void setStartSeason(int startSeason) {
        this.startSeason = startSeason;
    }

    public boolean isSeasonIsYear() {
        return seasonIsYear;
    }

    public void setSeasonIsYear(boolean seasonIsYear) {
        this.seasonIsYear = seasonIsYear;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Bitmap getHdImage() {
        return hdImage;
    }

    public void setHdImage(Bitmap hdImage) {
        this.hdImage = hdImage;
    }

    public String getHdImagePath() {
        return hdImagePath;
    }

    public void setHdImagePath(String hdImagePath) {
        this.hdImagePath = hdImagePath;
    }
}
