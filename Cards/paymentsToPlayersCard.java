package Cards;

import serviceCardHandlers.paymentsToPlayersCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class paymentsToPlayersCard extends Card {

    public int amountToPay;

    public paymentsToPlayersCard(int amount, String message, cardDeckType deck)
    {
        super(message, deck, new paymentsToPlayersCardHandler());
        amountToPay = amount;
    }

}