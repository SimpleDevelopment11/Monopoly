package clientSpaceHandlers;

import app.Player;
import gameSpaces.boardSpace;

public abstract class basicHandler implements clientSpaceHandler {

    public boardSpace landedSpace;
    public Player currentPlayer;

    public synchronized void notifyThread()
    {
        notifyAll();
    }

}
