package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class freeParkingHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service) {

    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.freeParkingHandler();
    }

}