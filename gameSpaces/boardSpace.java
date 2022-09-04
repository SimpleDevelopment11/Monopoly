package gameSpaces;

import outcomeHandlers.serviceSpaceHandler;

public abstract class boardSpace {

    public int spacePosition;
    public String spaceName;
    public serviceSpaceHandler handler;

    public boardSpace(int position, String name, serviceSpaceHandler handler)
    {
        spacePosition = position;
        spaceName = name;
        this.handler = handler;
    }

}