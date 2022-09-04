package Cards;

import serviceCardHandlers.buildingPaymentsCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class buildingPaymentsCard extends Card {

    public int houseRate;
    public int hotelRate;

    public buildingPaymentsCard(int houseRate, int hotelRate, String message, cardDeckType deck)
    {
        super(message, deck, new buildingPaymentsCardHandler());
        this.houseRate = houseRate;
        this.hotelRate = hotelRate;
    }

}