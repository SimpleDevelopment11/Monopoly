package clientSpaceHandlers;

import app.ClientGame;
import app.monopolyService;

public interface clientSpaceHandler {

    boolean handleLandedEvent(ClientGame parentGame, monopolyService service) throws InterruptedException;

    void notifyThread();

}
