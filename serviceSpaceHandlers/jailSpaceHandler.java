package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class jailSpaceHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service)
    {

    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.jailSpaceHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.jailSpaceHandler();
    }

}
