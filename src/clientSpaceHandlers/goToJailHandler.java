package clientSpaceHandlers;

import app.ClientGame;
import app.guiBoard;
import app.monopolyService;

public class goToJailHandler extends basicHandler {

    public boolean handleLandedEvent(ClientGame parentGame, monopolyService service)
    {
        guiBoard myGui = parentGame.myGui;
        myGui.createMessageDialog("Tough One", "Time to do Time...", "You landed on go to jail, so that is where you shall go.");
        myGui.movePlayerPiece(currentPlayer);
        return true;
    }

}