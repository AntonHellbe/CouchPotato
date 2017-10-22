package se.mah.couchpotato;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public abstract class DownloadRequest {
    private String query;

    public DownloadRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
