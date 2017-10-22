package se.mah.couchpotato;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THE DESERIALIZE CLASS OF DOOOOOOOOOOOOOOOOOOOOM
 * CONVERTS ALL DA JSON OBJECTS FROM DA TVMAZE INTO JAVA OBJECTS
 *
 * GOOD FUCKING YARD
 */



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
        "_links",
        "_embedded"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvShow {

    public TvShow() {}

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
    private Object image;
    @JsonProperty("summary")
    private Object summary;
    @JsonProperty("_links")
    private Links links;
    @JsonProperty("_embedded")
    private Embedded embedded;
    @JsonProperty("Show")
    private Show show;


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

    @JsonProperty("show")
    public Show getShow()
    {
        return this.show;
    }

    @JsonProperty("show")
    public void setShow(Show show)
    {
        this.show = show;
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
    public Object getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Object image) {
        this.image = image;
    }

    @JsonProperty("summary")
    public Object getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Object summary) {
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

    @JsonProperty("_embedded")
    public Embedded getEmbedded() {
        return embedded;
    }

    @JsonProperty("_embedded")
    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
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
            "name",
            "code",
            "timezone"
    })
    static class Country {

        public Country(){}

        @JsonProperty("name")
        private String name;
        @JsonProperty("code")
        private String code;
        @JsonProperty("timezone")
        private String timezone;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("code")
        public String getCode() {
            return code;
        }

        @JsonProperty("code")
        public void setCode(String code) {
            this.code = code;
        }

        @JsonProperty("timezone")
        public String getTimezone() {
            return timezone;
        }

        @JsonProperty("timezone")
        public void setTimezone(String timezone) {
            this.timezone = timezone;
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
            "show"
    })
    static class Embedded {

        public Embedded(){}

        @JsonProperty("show")
        private Show show;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("show")
        public Show getShow() {
            return show;
        }

        @JsonProperty("show")
        public void setShow(Show show) {
            this.show = show;
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
            "tvrage",
            "thetvdb",
            "imdb"
    })
    static class Externals {

        public Externals(){}

        @JsonProperty("tvrage")
        private Object tvrage;
        @JsonProperty("thetvdb")
        private Integer thetvdb;
        @JsonProperty("imdb")
        private String imdb;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("tvrage")
        public Object getTvrage() {
            return tvrage;
        }

        @JsonProperty("tvrage")
        public void setTvrage(Object tvrage) {
            this.tvrage = tvrage;
        }

        @JsonProperty("thetvdb")
        public Integer getThetvdb() {
            return thetvdb;
        }

        @JsonProperty("thetvdb")
        public void setThetvdb(Integer thetvdb) {
            this.thetvdb = thetvdb;
        }

        @JsonProperty("imdb")
        public String getImdb() {
            return imdb;
        }

        @JsonProperty("imdb")
        public void setImdb(String imdb) {
            this.imdb = imdb;
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
            "medium",
            "original"
    })
    public static class Image {

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

        public Links(){}

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
            "self",
            "previousepisode",
            "nextepisode"
    })
    static class Links_ {
        public Links_(){}

        @JsonProperty("self")
        private Self_ self;
        @JsonProperty("previousepisode")
        private Previousepisode previousepisode;
        @JsonProperty("nextepisode")
        private Nextepisode nextepisode;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("self")
        public Self_ getSelf() {
            return self;
        }

        @JsonProperty("self")
        public void setSelf(Self_ self) {
            this.self = self;
        }

        @JsonProperty("previousepisode")
        public Previousepisode getPreviousepisode() {
            return previousepisode;
        }

        @JsonProperty("previousepisode")
        public void setPreviousepisode(Previousepisode previousepisode) {
            this.previousepisode = previousepisode;
        }

        @JsonProperty("nextepisode")
        public Nextepisode getNextepisode() {
            return nextepisode;
        }

        @JsonProperty("nextepisode")
        public void setNextepisode(Nextepisode nextepisode) {
            this.nextepisode = nextepisode;
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
            "id",
            "name",
            "country"
    })
    static class Network {

        public Network(){}

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("country")
        private Country country;
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

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("country")
        public Country getCountry() {
            return country;
        }

        @JsonProperty("country")
        public void setCountry(Country country) {
            this.country = country;
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
    static class Nextepisode {

        public Nextepisode(){}

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "href"
    })
    static class Previousepisode {

        public Previousepisode(){}

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "average"
    })
    static class Rating {

        public Rating(){}

        @JsonProperty("average")
        private Integer average;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("average")
        public Integer getAverage() {
            return average;
        }

        @JsonProperty("average")
        public void setAverage(Integer average) {
            this.average = average;
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
            "time",
            "days"
    })
    static class Schedule {

        public Schedule(){}

        @JsonProperty("time")
        private String time;
        @JsonProperty("days")
        private List<String> days = null;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("time")
        public String getTime() {
            return time;
        }

        @JsonProperty("time")
        public void setTime(String time) {
            this.time = time;
        }

        @JsonProperty("days")
        public List<String> getDays() {
            return days;
        }

        @JsonProperty("days")
        public void setDays(List<String> days) {
            this.days = days;
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
        @JsonCreator
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


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "href"
    })
    static class Self_ {
//        @JsonCreator
        public Self_(){}

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "url",
            "name",
            "type",
            "language",
            "genres",
            "status",
            "runtime",
            "premiered",
            "officialSite",
            "schedule",
            "rating",
            "weight",
            "network",
            "webChannel",
            "externals",
            "image",
            "summary",
            "updated",
            "_links"
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Show {

//        public Show(@JsonProperty("genres") final List<Object> genres, @JsonProperty("officialSite") final Object officialsite, @JsonProperty("schedule") final Schedule sched,
//        @JsonProperty("rating") final Rating rat, @JsonProperty("network") final Network net, @JsonProperty("webchannel") final Object webchan, @JsonProperty("externals") final Externals ext,
//                    @JsonProperty("image") final Image im, @JsonProperty("_links") final Links_ link){}
//        @JsonCreator
        public Show(){}

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("url")
        private String url;
        @JsonProperty("name")
        private String name;
        @JsonProperty("type")
        private String type;
        @JsonProperty("language")
        private String language;
        @JsonProperty("genres")
        private List<Object> genres = null;
        @JsonProperty("status")
        private String status;
        @JsonProperty("runtime")
        private Integer runtime;
        @JsonProperty("premiered")
        private String premiered;
        @JsonProperty("officialSite")
        private Object officialSite;
        @JsonProperty("schedule")
        private Schedule schedule;
        @JsonProperty("rating")
        private Rating rating;
        @JsonProperty("weight")
        private Integer weight;
        @JsonProperty("network")
        private Network network;
        @JsonProperty("webChannel")
        private Object webChannel;
        @JsonProperty("externals")
        private Externals externals;
        @JsonProperty("image")
        private Image image;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("updated")
        private Integer updated;
        @JsonProperty("_links")
        private Links_ links;
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

        @JsonProperty("type")
        public String getType() {
            return type;
        }

        @JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }

        @JsonProperty("language")
        public String getLanguage() {
            return language;
        }

        @JsonProperty("language")
        public void setLanguage(String language) {
            this.language = language;
        }

        @JsonProperty("genres")
        public List<Object> getGenres() {
            return genres;
        }

        @JsonProperty("genres")
        public void setGenres(List<Object> genres) {
            this.genres = genres;
        }

        @JsonProperty("status")
        public String getStatus() {
            return status;
        }

        @JsonProperty("status")
        public void setStatus(String status) {
            this.status = status;
        }

        @JsonProperty("runtime")
        public Integer getRuntime() {
            return runtime;
        }

        @JsonProperty("runtime")
        public void setRuntime(Integer runtime) {
            this.runtime = runtime;
        }

        @JsonProperty("premiered")
        public String getPremiered() {
            return premiered;
        }

        @JsonProperty("premiered")
        public void setPremiered(String premiered) {
            this.premiered = premiered;
        }

        @JsonProperty("officialSite")
        public Object getOfficialSite() {
            return officialSite;
        }

        @JsonProperty("officialSite")
        public void setOfficialSite(Object officialSite) {
            this.officialSite = officialSite;
        }

        @JsonProperty("schedule")
        public Schedule getSchedule() {
            return schedule;
        }

        @JsonProperty("schedule")
        public void setSchedule(Schedule schedule) {
            this.schedule = schedule;
        }

        @JsonProperty("rating")
        public Rating getRating() {
            return rating;
        }

        @JsonProperty("rating")
        public void setRating(Rating rating) {
            this.rating = rating;
        }

        @JsonProperty("weight")
        public Integer getWeight() {
            return weight;
        }

        @JsonProperty("weight")
        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        @JsonProperty("network")
        public Network getNetwork() {
            return network;
        }

        @JsonProperty("network")
        public void setNetwork(Network network) {
            this.network = network;
        }

        @JsonProperty("webChannel")
        public Object getWebChannel() {
            return webChannel;
        }

        @JsonProperty("webChannel")
        public void setWebChannel(Object webChannel) {
            this.webChannel = webChannel;
        }

        @JsonProperty("externals")
        public Externals getExternals() {
            return externals;
        }

        @JsonProperty("externals")
        public void setExternals(Externals externals) {
            this.externals = externals;
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

        @JsonProperty("updated")
        public Integer getUpdated() {
            return updated;
        }

        @JsonProperty("updated")
        public void setUpdated(Integer updated) {
            this.updated = updated;
        }

        @JsonProperty("_links")
        public Links_ getLinks() {
            return links;
        }

        @JsonProperty("_links")
        public void setLinks(Links_ links) {
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

        @Override
        public String toString() {
            return this.name + " " + this.language + this.getStatus() + getRating();
        }
    }
}
