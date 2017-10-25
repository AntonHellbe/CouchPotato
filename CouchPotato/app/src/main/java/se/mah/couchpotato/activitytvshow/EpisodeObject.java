package se.mah.couchpotato.activitytvshow;

/**
 * Created by Anton on 2017-10-23.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "url",
        "name",
        "season",
        "number",
        "airdate",
        "airtime",
        "airstamp",
        "runtime",
        "image",
        "summary",
        "_links"
})
public class EpisodeObject {

    public EpisodeObject(){}

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("name")
    private String name;
    @JsonProperty("season")
    private Integer season;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("airdate")
    private String airdate;
    @JsonProperty("airtime")
    private String airtime;
    @JsonProperty("airstamp")
    private String airstamp;
    @JsonProperty("runtime")
    private Integer runtime;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("_links")
    private Links links;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("season")
    public Integer getSeason() {
        return season;
    }

    @JsonProperty("season")
    public void setSeason(Integer season) {
        this.season = season;
    }

    @JsonProperty("number")
    public Integer getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(Integer number) {
        this.number = number;
    }

    @JsonProperty("airdate")
    public String getAirdate() {
        return airdate;
    }

    @JsonProperty("airdate")
    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    @JsonProperty("airtime")
    public String getAirtime() {
        return airtime;
    }

    @JsonProperty("airtime")
    public void setAirtime(String airtime) {
        this.airtime = airtime;
    }

    @JsonProperty("airstamp")
    public String getAirstamp() {
        return airstamp;
    }

    @JsonProperty("airstamp")
    public void setAirstamp(String airstamp) {
        this.airstamp = airstamp;
    }

    @JsonProperty("runtime")
    public Integer getRuntime() {
        return runtime;
    }

    @JsonProperty("runtime")
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Image image) {
        this.image = image;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "medium",
        "original"
})
static class Image {

    public Image(){}

    @JsonProperty("medium")
    private String medium;
    @JsonProperty("original")
    private String original;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("medium")
    public String getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(String medium) {
        this.medium = medium;
    }

    @JsonProperty("original")
    public String getOriginal() {
        return original;
    }

    @JsonProperty("original")
    public void setOriginal(String original) {
        this.original = original;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self"
})
static class Links {

    Links(){}

    @JsonProperty("self")
    private Self self;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("self")
    public Self getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(Self self) {
        this.self = self;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "href"
})
static class Self {

    public Self(){}

    @JsonProperty("href")
    private String href;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
}