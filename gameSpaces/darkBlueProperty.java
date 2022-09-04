package gameSpaces;

public class darkBlueProperty extends avenueProperty {

    public darkBlueProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 2, rentPrices, 200, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}