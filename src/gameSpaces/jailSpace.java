package gameSpaces;

import serviceSpaceHandlers.jailSpaceHandler;

public class jailSpace extends boardSpace{

    public jailSpace(int position, String name)
    {
        super(position, name, new jailSpaceHandler());
    }

}