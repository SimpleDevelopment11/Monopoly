package gameSpaces;

import outcomeHandlers.goSpaceHandler;

public class goSpace extends boardSpace {

    public int bonusLandAmount;

    public goSpace(int bonus, int position, String name)
    {
        super(position, name, new goSpaceHandler());
        bonusLandAmount = bonus;
    }

}