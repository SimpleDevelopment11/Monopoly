package gameSpaces;

import outcomeHandlers.goToJailHandler;

public class goToJailSpace extends boardSpace {

    public int jailPosition;

    public goToJailSpace(int jailPosition, int position, String name)
    {
        super(position, name, new goToJailHandler());
        this.jailPosition = jailPosition;
    }

}