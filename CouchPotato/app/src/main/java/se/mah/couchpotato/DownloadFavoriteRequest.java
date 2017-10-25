package se.mah.couchpotato;

/**
 * Created by robin on 25/10/2017.
 */

public class DownloadFavoriteRequest extends DownloadRequest {

    private FavoriteListener callback;

    public DownloadFavoriteRequest(String query, FavoriteListener callback) {
        super(query);
        this.callback = callback;
    }

    public FavoriteListener getCallback() {
        return callback;
    }

    public void setCallback(FavoriteListener callback) {
        this.callback = callback;
    }
}
