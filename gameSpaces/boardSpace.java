package gameSpaces;

import outcomeHandlers.serviceSpaceHandler;

public abstract class boardSpace {

    public int spacePosition;
    public String spaceName;
    public serviceSpaceHandler handler;

    public boardSpace(int position, String name)
    {
        spacePosition = position;
        spaceName = name;
    }

}