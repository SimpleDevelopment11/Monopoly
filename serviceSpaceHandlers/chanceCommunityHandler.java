package serviceSpaceHandlers;

import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;

public class chanceCommunityHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service)
    {

    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.chanceCommunityHandler();
    }

}