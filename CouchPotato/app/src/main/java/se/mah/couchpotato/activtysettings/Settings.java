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

    public Settings() {
        nsfw = false;
        country = "US";
        language = "English";
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

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel parcel) {
            boolean tempTheme, tempNsfw;
            tempNsfw = (parcel.readInt() == 0) ? false : true;
            return new Settings(tempNsfw, parcel.readString(),
                    parcel.readString(), parcel.readInt());
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
