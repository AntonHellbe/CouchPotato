package se.mah.couchpotato;

import android.util.Log;

/**
 * Created by Anton on 2017-10-19.
 */

public class UrlBuilder {

    public static final String BASE_URL_TV_MAZE = "http://api.tvmaze.com/";
    public static final String SHOW_SEARCH = BASE_URL_TV_MAZE + "search/shows?q=";
    public static final String SINGLE_SHOW_SEARCH = BASE_URL_TV_MAZE + "singlesearch/shows?q=";
    public static final String TODAYS_SCHEDULE = BASE_URL_TV_MAZE + "schedule?country=";  //TODO settings
    public static final String FULL_SCHEDULE = BASE_URL_TV_MAZE + "schedule/full";  //DANGER ZONE
    public static final String SHOW_BY_ID = BASE_URL_TV_MAZE + "shows/";
    public static final String SHOW_BY_IMDB = BASE_URL_TV_MAZE + "lookup/shows?imdb=";
    public static final String SEASONS = "/seasons";
    public static final String EPISODES = "/episodes";
    public static final String EPISODE_BY_SEASON = "/episodebynumber?season=";
    public static final String EPISODE_BY_NUMBER = "&number=";
    public static final String BASE_URL_OMDB = "http://www.omdbapi.com/?i=";
    public static final String API_KEY = "&apikey=";


    public UrlBuilder(){}

    public String showSearch(String searchParam){
        return SHOW_SEARCH + searchParam;
    }

    public String singleShowSearch(String searchParam){
        return SINGLE_SHOW_SEARCH + searchParam;
    }

    public String getShowById(int id){
        return SHOW_BY_ID + id;
    }

    public String getShowByImdb(String imdb){
        return SHOW_BY_IMDB + imdb;
    }

    public String getEpisodeList(String id){
        return SHOW_BY_ID + id + EPISODES;
    }

    public String getEpisodeByNumber(String id, String season, String episode){
        return SHOW_BY_ID + id + EPISODE_BY_SEASON + season + EPISODE_BY_NUMBER + episode;
    }

    public String getSeasons(int id){
        return SHOW_BY_ID + id + SEASONS;
    }

    public String ratingById(String id){
        return BASE_URL_OMDB + id + API_KEY;
    }

    public String getScheduleByCountry(String country){
        return TODAYS_SCHEDULE + country;
    }




}
