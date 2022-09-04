package gameSpaces;

public class lightBlueProperty extends avenueProperty{

    public lightBlueProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 50, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}