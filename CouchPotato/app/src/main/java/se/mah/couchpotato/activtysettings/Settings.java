package se.mah.couchpotato.activtysettings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import se.mah.couchpotato.R;

/**
 * @author Jonatan Fridsten
 *         <p>
 *         Class that stores the information that the user could choose
 */

public class Settings implements Parcelable {

    private boolean nsfw;
    private boolean notification;
    private String language;
    private String country;
    private long timeZone;
    private long notificationTime;
    private int position_count;

    public static final String NSFW = "nsfw";
    public static final String LANG = "language";
    public static final String COUNT = "country";
    public static final String POSCOUNT = "posCount";
    public static final String NOTIFICATION = "notification";
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final String TIME_ZONE = "timezone";


    public Settings(Context context) {
        nsfw = true;
        country = "US";
        String res = Resources.getSystem().getConfiguration().locale.getLanguage();
        if (res.equals("sv")){
            language = context.getResources().getString(R.string.settings_language_swedish);
        }else if(res.equals("en")){
            language = context.getResources().getString(R.string.settings_language_english);
        }else {
            language = context.getResources().getString(R.string.settings_language_english);
        }
        Log.v("Settings","Android device language:" + res);
        position_count = 1;
        notification = true;
        notificationTime = 1800; //18 * 60 * 1000 + 0 * 1000
        timeZone = 0;
    }

    public Settings(boolean nsfw, boolean notification, String language, String country, int position_count, long notificationTime, long timeZone) {
        this.nsfw = nsfw;
        this.notification = notification;
        this.language = language;
        this.country = country;
        this.position_count = position_count;
        this.notificationTime = notificationTime;
        this.timeZone = timeZone;
    }


    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel parcel) {
            boolean tempNotification, tempNsfw;
            tempNsfw = (parcel.readInt() == 0) ? false : true;
            tempNotification = (parcel.readInt() == 0) ? false : true;
            return new Settings(tempNsfw, tempNotification, parcel.readString(),
                    parcel.readString(), parcel.readInt(),
                    parcel.readLong(), parcel.readLong());
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
        parcel.writeInt(notification ? 1 : 0);
        parcel.writeString(language);
        parcel.writeString(country);
        parcel.writeInt(position_count);
        parcel.writeLong(notificationTime);
        parcel.writeLong(timeZone);
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

    public int getPosition_count() {
        return position_count;
    }

    public void setPosition_count(int position_count) {
        this.position_count = position_count;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public long getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(long timeZone) {
        this.timeZone = timeZone;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }
}
