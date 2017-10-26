package se.mah.couchpotato.activtysettings;

import android.app.Application;

/**
 * Created by Anton on 2017-10-26.
 */

public class ApplicationVisible extends Application {
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
