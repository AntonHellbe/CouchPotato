package se.mah.couchpotato;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class DownloadImageRequest extends DownloadRequest {

    private String id;
    private PosterListener posterListener;

    public DownloadImageRequest(String query, String id, PosterListener posterListener) {
        super(query);
        this.id = id;
        this.posterListener = posterListener;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PosterListener getPosterListener() {
        return posterListener;
    }

    public void setPosterListener(PosterListener posterListener) {
        this.posterListener = posterListener;
    }
}
