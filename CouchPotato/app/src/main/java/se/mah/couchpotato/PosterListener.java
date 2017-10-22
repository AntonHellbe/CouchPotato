package se.mah.couchpotato;

import android.graphics.Bitmap;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public interface PosterListener {
    void onPosterDownloaded(String id, Bitmap bitmap);
}
