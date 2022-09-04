package gameSpaces;

public class pinkProperty extends avenueProperty {

    public pinkProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 100, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}