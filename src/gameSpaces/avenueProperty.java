package gameSpaces;

public class avenueProperty extends Property {

    public avenueProperty(int initialCost, int colorGroupNum, int rentPrices[], int buildCost, String name, int position)
    {
        super(initialCost, colorGroupNum, rentPrices, buildCost, name, position);
    }

    public int getRent(int roll)
    {
        if (!this.isMortgaged)
        {
            if (!this.partOfMonopoly())
            {
                return this.rentPrices[this.buildingLevel];
            }
            else
            {
                //If there are no buildings, double rent
                if (this.buildingLevel == 0)
                {
                    return (2 * this.rentPrices[0]);
                }
                else
                {
                    return (this.rentPrices[this.buildingLevel]);
                }
            }
        }
        else
        {
            //No rent if the property is mortgaged
            return 0;
        }
    }

    //This method does not consider whether or not the player can afford new buildings
    public boolean canRaiseOnSingleProperty(boolean evenBuild)
    {
        if (this.partOfMonopoly())
        {
            if (this.isMortgaged == false)
            {
                //If there are still more buildings that can be built
                if (this.buildingLevel < this.rentPrices.length) {
                    //Check for even build and if other properties are mortgaged
                    if (evenBuild) {
                        for (Property property : this.allPropertiesInColorGroup) {
                            //If this property already has more buildings than any other property in the set, return false
                            if (this.buildingLevel > property.buildingLevel) {
                                return false;
                            }

                            if (property.isMortgaged)
                            {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canRaiseOnSet()
    {
        if (this.partOfMonopoly())
        {
            for (Property property: this.allPropertiesInColorGroup)
            {
                //If any of the properties is already at maximum build level, cannot build on set
                if (property.buildingLevel == (property.rentPrices.length - 1) || property.isMortgaged)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public boolean canTakeDownOnSingleProperty(boolean evenBuild)
    {
        if (this.buildingLevel > 0)
        {
            //Check for even build
            if (evenBuild)
            {
                for (Property property: this.allPropertiesInColorGroup)
                {
                    //If this property already has less buildings than any other property in the set, return false
                    if (this.buildingLevel < property.buildingLevel)
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean canTakeDownOnSet()
    {
        for (Property property: this.allPropertiesInColorGroup)
        {
            //If any property in the color group currently has no buildings, return false
            if (property.buildingLevel == 0)
            {
                return false;
            }
        }
        return true;
    }

    public int getTakeDownValue()
    {
        return (this.buildingCost / 2);
    }

}