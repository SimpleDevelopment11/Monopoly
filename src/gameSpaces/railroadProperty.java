package gameSpaces;

public class railroadProperty extends Property implements PropertyInterface{

    public railroadProperty(int initialCost, int[] prices, String name, int position)
    {
        //Prices will have one element which is initial rent cost
        super(initialCost, 4, prices, -1, name, position);
    }

    public int getRent(int roll)
    {
        if (this.isMortgaged)
        {
            return 0;
        }
        int railroadsOwnedByPlayerNum = 0;
        for (Property property: this.allPropertiesInColorGroup)
        {
            if (property.ownedBy == this.ownedBy)
            {
                railroadsOwnedByPlayerNum++;
            }
        }
        railroadsOwnedByPlayerNum--;
        int netMultiplier = (int) Math.pow(2, railroadsOwnedByPlayerNum);
        return (this.rentPrices[0] * netMultiplier);
    }

    public boolean canRaiseOnSingleProperty(boolean evenBuild)
    {
        return false;
    }

    public boolean canRaiseOnSet()
    {
        return false;
    }

    public boolean canTakeDownOnSingleProperty(boolean evenBuild)
    {
        return false;
    }

    public boolean canTakeDownOnSet()
    {
        return false;
    }

}