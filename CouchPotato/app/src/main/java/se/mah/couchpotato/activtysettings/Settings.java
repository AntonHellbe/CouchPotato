package se.mah.couchpotato.activtysettings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Jonatan Fridsten
 */

public class Settings implements Parcelable {

    private boolean nsfw;
    private String language;
    private String country;
    private int timeZone;
    private int position_lang;
    private int position_count;


    public Settings() {
        nsfw = false;
        country = "US";
        language = "English";
        position_count = 1;
        position_lang = 1;
    }

    public Settings(boolean nsfw, String language, String country) {
        this.nsfw = nsfw;
        this.language = language;
        this.country = country;
    }

    public Settings(boolean nsfw, String language, String country, int timeZone) {
        this.nsfw = nsfw;
        this.language = language;
        this.country = country;
        this.timeZone = timeZone;
    }

    public Settings(boolean nsfw, String language, String country, int timeZone, int position_lang, int position_count) {
        this.nsfw = nsfw;
        this.language = language;
        this.country = country;
        this.timeZone = timeZone;
        this.position_lang = position_lang;
        this.position_count = position_count;
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel parcel) {
            boolean tempTheme, tempNsfw;
            tempNsfw = (parcel.readInt() == 0) ? false : true;
            return new Settings(tempNsfw, parcel.readString(),
                    parcel.readString(), parcel.readInt(),
                    parcel.readInt(),parcel.readInt());
        }

        @Override
        public Settings[] newArray(int i) {
            return new Settings[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(nsfw ? 1 : 0);
        parcel.writeString(language);
        parcel.writeString(country);
        parcel.writeInt(timeZone);
        parcel.writeInt(position_lang);
        parcel.writeInt(position_count);
    }


    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public int getPosition_lang() {
        return position_lang;
    }

    public void setPosition_lang(int position_lang) {
        this.position_lang = position_lang;
    }

    public int getPosition_count() {
        return position_count;
    }

    public void setPosition_count(int position_count) {
        this.position_count = position_count;
    }
}
