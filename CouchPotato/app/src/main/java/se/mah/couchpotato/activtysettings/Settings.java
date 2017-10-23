package se.mah.couchpotato.activtysettings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Jonatan Fridsten
 */

public class Settings implements Parcelable {

    private boolean theme;
    private boolean nsfw;
    private String language;
    private String country;
    private int timeZone;

    public Settings(){

    }

    public Settings(boolean theme, boolean nsfw, String language, String country) {
        this.theme = theme;
        this.nsfw = nsfw;
        this.language = language;
        this.country = country;
    }

    public Settings(boolean theme, boolean nsfw, String language, String country, int timeZone) {
        this.theme = theme;
        this.nsfw = nsfw;
        this.language = language;
        this.country = country;
        this.timeZone = timeZone;
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel parcel) {
            boolean tempTheme,tempNsfw;
            tempTheme = (parcel.readInt() == 0) ? false : true;
            tempNsfw = (parcel.readInt() == 0) ? false : true;
            return new Settings(tempTheme,tempNsfw,parcel.readString(),
                    parcel.readString(),parcel.readInt());
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
        parcel.writeInt(theme ? 1 : 0);
        parcel.writeInt(nsfw ? 1 : 0);
        parcel.writeString(language);
        parcel.writeString(country);
        parcel.writeInt(timeZone);
    }


    public boolean isTheme() {
        return theme;
    }

    public void setTheme(boolean theme) {
        this.theme = theme;
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
}
