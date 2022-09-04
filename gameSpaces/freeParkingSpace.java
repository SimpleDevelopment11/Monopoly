package gameSpaces;

import serviceSpaceHandlers.freeParkingHandler;

public class freeParkingSpace extends boardSpace {

    public freeParkingSpace(int position, String name)
    {
        super(position, name, new freeParkingHandler());
    }

}