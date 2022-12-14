package serviceSpaceHandlers;

import app.monopolyService;

public interface serviceSpaceHandler {

    void handleLandedEvent(monopolyService monoService);

    serviceSpaceHandler getNewHandler();

    clientSpaceHandlers.clientSpaceHandler getClientHandler();

}
