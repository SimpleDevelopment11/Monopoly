package gameSpaces;

public class yellowProperty extends avenueProperty {

    public yellowProperty(int initialCost, int rentPrices[], String name, int position)
    {
        super(initialCost, 3, rentPrices, 150, name, position);
    }

    public int getRent()
    {
        return super.rentPrices[super.buildingLevel];
    }

}