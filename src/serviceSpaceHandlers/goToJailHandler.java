package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class goToJailHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service)
    {
        service.server.goToJail();
    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.goToJailHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.goToJailHandler();
    }

}