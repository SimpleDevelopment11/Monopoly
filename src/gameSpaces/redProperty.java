package gameSpaces;

public class redProperty extends avenueProperty {

    public redProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 150, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}