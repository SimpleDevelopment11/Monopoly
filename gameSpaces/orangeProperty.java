package gameSpaces;

public class orangeProperty extends avenueProperty {

    public orangeProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 100, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}