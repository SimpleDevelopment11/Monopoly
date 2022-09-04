package Cards;

import serviceCardHandlers.collectionsFromPlayersCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class collectionsFromPlayersCard extends Card {

    public int amountToCollect;

    public collectionsFromPlayersCard(int amount, String message, cardDeckType deck)
    {
        super(message, deck, new collectionsFromPlayersCardHandler());
        amountToCollect = amount;
    }

}