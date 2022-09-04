package gameSpaces;

import app.Player;
import outcomeHandlers.propertyHandler;

public abstract class Property extends boardSpace implements PropertyInterface {

    public int initialCost;
    public int numberOfPropertiesInColorGroup;
    public Property[] allPropertiesInColorGroup;
    private int propertiesAdded = 0;
    //Array of rent prices due to number of buildings
    public int[] rentPrices;
    public int buildingCost;
    //Int of the current building number which corresponds to values in rentPrices
    public int buildingLevel;
    public boolean isMortgaged;
    //If the bank owns property, ownedBy is null
    public Player ownedBy;

    public Property(int inCost, int colorGroupNum, int[] prices, int buildCost, String name, int position)
    {
        super(position, name, new propertyHandler());
        numberOfPropertiesInColorGroup = colorGroupNum;
        allPropertiesInColorGroup = new Property[numberOfPropertiesInColorGroup];
        initialCost = inCost;
        rentPrices = prices;
        buildingLevel = 0;
        buildingCost = buildCost;
        //All properties are initially unMortgaged
        isMortgaged = false;
        //All properties are initially owned by the bank
        ownedBy = null;
        addPropertyToSet(this);
    }

    public void addPropertyToSet(boardSpace ... args)
    {
        for (boardSpace propertyToAdd: args)
        {
            this.allPropertiesInColorGroup[propertiesAdded] = (Property) propertyToAdd;
            propertiesAdded++;
        }
    }


    public boolean partOfMonopoly()
    {

        for (Property property: this.allPropertiesInColorGroup)
        {
            if (!(property.ownedBy == this.ownedBy))
            {
                return false;
            }
        }

        return true;
    }

    public boolean canMortgage()
    {
        if (this.isMortgaged)
        {
            return false;
        }
        if (partOfMonopoly())
        {
            for (Property ave : allPropertiesInColorGroup)
            {
                if (ave.buildingLevel > 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canUnMortgage()
    {
        if (this.isMortgaged == false)
        {
            return false;
        }
        return true;
    }

    public int getMortgageValue()
    {
        int value;
        value = this.initialCost / 2;
        return value;
    }

    public int getUnMortgageCost()
    {
        int cost;
        cost = this.initialCost / 2;
        cost = (int) (cost * 1.1);
        return cost;
    }

}