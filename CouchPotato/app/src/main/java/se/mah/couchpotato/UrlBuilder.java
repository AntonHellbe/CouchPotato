package se.mah.couchpotato;

/**
 * Created by Anton on 2017-10-19.
 */

public class UrlBuilder {

    public static final String BASE_URL = "http://api.tvmaze.com/";
    public static final String SHOW_SEARCH = BASE_URL + "search/shows?q=";
    public static final String SINGLE_SHOW_SEARCH = BASE_URL + "singlesearch/shows?q=";
    public static final String TODAYS_SCHEDULE = BASE_URL + "schedule?country=SE";
    public static final String FULL_SCHEDULE = BASE_URL + "schedule/full";
    public static final String SHOW_BY_ID = BASE_URL + "shows/";
    public static final String SHOW_BY_IMDB = BASE_URL + "lookup/shows?imdb=";
    public static final String SEASONS = "/seasons";
    public static final String EPISODES = "/episodes";
    public static final String EPISODE_BY_SEASON = "/episodebynumber?season=";
    public static final String EPISODE_BY_NUMBER = "&number=";


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

    public String getEpisodeList(int id){
        return SHOW_BY_ID + id + EPISODES;
    }

    public String getEpisodeByNumber(int id, int season, int episode){
        return SHOW_BY_ID + id + EPISODE_BY_SEASON + season + EPISODE_BY_NUMBER + episode;
    }

    public String getSeasons(int id){
        return SHOW_BY_ID + id + SEASONS;
    }




}
