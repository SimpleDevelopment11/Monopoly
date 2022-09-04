package clientSpaceHandlers;

import app.ClientGame;
import app.monopolyService;

public interface clientSpaceHandler {

    void handleLandedEvent(ClientGame parentGame, monopolyService service) throws InterruptedException;

}
