package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class freeParkingHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service) {

    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.freeParkingHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.freeParkingHandler();
    }

}