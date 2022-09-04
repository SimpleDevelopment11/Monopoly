package clientSpaceHandlers;

import app.ClientGame;
import app.monopolyService;

public class freeParkingHandler extends basicHandler {

    public boolean handleLandedEvent(ClientGame parentGame, monopolyService service) {
        return true;
    }

}