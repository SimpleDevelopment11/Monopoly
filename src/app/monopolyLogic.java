package app;

import gameSpaces.Property;
import gameSpaces.avenueProperty;

import java.util.Random;

public class monopolyLogic {

    public monopolyLogic()
    {

    }

    //To be used if multi-client feature is implemented
    public boolean canRoll(Player player)
    {
        if (!player.isBankrupt)
        {
            return true;
        }
        return false;
    }

    public int[] generateRoll()
    {
        Random rand = new Random();
        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        return new int[]{die1, die2};
    }

    public boolean isDoubles(int[] roll)
    {
        if (roll[0] == roll[1])
        {
            return true;
        }
        return false;
    }

    public boolean canHandlePayment(Player thePlayer, int amountToPay)
    {
        int netWorth = thePlayer.readyCash;

        for (Property property: thePlayer.properties)
        {

            if (property.isMortgaged == false)
            {
                if (property.buildingLevel > 0)
                {
                    for (int counter = 0; counter < property.buildingLevel; counter++)
                    {
                        netWorth += ((avenueProperty) property).getTakeDownValue();
                    }
                }
                netWorth += property.initialCost / 2;
            }
        }

        if (netWorth >= amountToPay)
        {
            return true;
        }

        return false;
    }

    public int getBuildingRepairsCardPaymentAmount(Player thePlayer, int houseRate, int hotelRate)
    {
        int paymentAmount = 0;

        for (Property property : thePlayer.properties)
        {
            if (property.buildingLevel < 5)
            {
                paymentAmount += houseRate * property.buildingLevel;
            }
            else if (property.buildingLevel == 5)
            {
                paymentAmount += hotelRate;
            }
        }

        return paymentAmount;
    }

    public boolean canBuyProperty(Property propertySpace, monopolyService service)
    {
        if (propertySpace.ownedBy == null && service.canPay(propertySpace.initialCost))
        {
            return true;
        }
        return false;
    }

}