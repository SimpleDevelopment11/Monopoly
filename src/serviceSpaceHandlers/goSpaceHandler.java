package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class goSpaceHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service)
    {
        service.server.earnGoMoney();
    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.goSpaceHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.goSpaceHandler();
    }

}