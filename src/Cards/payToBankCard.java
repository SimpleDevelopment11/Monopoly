package Cards;

import serviceCardHandlers.payToBankCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class payToBankCard extends Card {

    public int paymentAmount;

    public payToBankCard(String message, int amount, cardDeckType deck)
    {
        super(message, deck, new payToBankCardHandler());
        paymentAmount = amount;
    }

}