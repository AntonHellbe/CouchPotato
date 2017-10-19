package se.mah.couchpotato;

/**
 * Created by robin on 19/10/2017.
 */

public class Controller {

    /**
     * Temporary for testing
     * */
    private CommunicationService cs = new CommunicationService();
    public void sendTest() {
        cs.sendToURL("test");
    }
}
