package se.mah.couchpotato;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by Anton on 2017-10-19.
 */

@JsonDeserialize(using = CustomTvShowDeserializer.class)
public class TvShow  {
    private int id;
    private String name;
    private String language;
    private String officalSite;
    private String[] pictureUrls;
    private String[] genres;
    private String status;
    private String network;


    public TvShow(int id, String name, String language, String officalSite, String[] pictureUrl,
                  String[] genres){

        this.id = id;
        this.language = language;
        this.officalSite = officalSite;
        this.pictureUrls = pictureUrl;
        this.genres = genres;
    }

    public TvShow(){

    }

    @Override
    public String toString() {
        return this.id + " " + this.name + " " + this.language + " " + this.officalSite + " " + this.status + " " + this.network;
    }


    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getPictureUrl() {
        return pictureUrls;
    }

    public void setPictureUrl(String[] pictureUrl) {
        this.pictureUrls = pictureUrl;
    }

    public String getOfficalSite() {
        return officalSite;
    }

    public void setOfficalSite(String officalSite) {
        this.officalSite = officalSite;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
