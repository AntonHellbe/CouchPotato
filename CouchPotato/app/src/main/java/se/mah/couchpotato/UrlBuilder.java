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


    public UrlBuilder(){

    }

    public String showSearch(String searchParam){
        return SHOW_SEARCH + searchParam;
    }

    public String singleShowSearch(String searchParam){
        return SINGLE_SHOW_SEARCH + searchParam;
    }

    public String getShowById(int id){
        return SHOW_BY_ID + id;
    }






}
