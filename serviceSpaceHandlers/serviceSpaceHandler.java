package serviceSpaceHandlers;

import app.monopolyService;

public interface serviceSpaceHandler {

    void handleLandedEvent(monopolyService monoService);

    clientSpaceHandlers.clientSpaceHandler getClientHandler();

}
