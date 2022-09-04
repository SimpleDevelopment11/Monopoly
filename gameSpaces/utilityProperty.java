package gameSpaces;

public class utilityProperty extends Property {

    private int regularMultiplier;
    private int monopolyMultiplier;

    public utilityProperty(int initialCost, String name, int regMulti, int monoMulti, int position)
    {
        //Utilities charge rent based on roll not building level; therefore, rentPrices is sent to super as null
        super(initialCost, 2, null, -1, name, position);
        regularMultiplier = regMulti;
        monopolyMultiplier = monoMulti;
    }

    public int getRent(int roll)
    {
        if (this.isMortgaged)
        {
            return 0;
        }
        if (this.partOfMonopoly())
        {
            return (roll * monopolyMultiplier);
        }
        else
        {
            return (roll * regularMultiplier);
        }
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