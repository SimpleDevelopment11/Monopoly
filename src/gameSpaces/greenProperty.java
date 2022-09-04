package gameSpaces;

public class greenProperty extends avenueProperty {

    public greenProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 200, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}