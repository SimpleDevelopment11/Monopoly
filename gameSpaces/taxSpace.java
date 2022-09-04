package gameSpaces;

import outcomeHandlers.taxSpaceHandler;

public class taxSpace extends boardSpace {

    public int taxAmount;

    public taxSpace(int amount, int position, String name)
    {
        super(position, name, new taxSpaceHandler());
        taxAmount = amount;
    }

}