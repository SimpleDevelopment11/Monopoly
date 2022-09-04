package Cards;

import serviceCardHandlers.collectionCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class collectionCard extends Card {

    public int collectionAmount;

    public collectionCard(String message, int amount, cardDeckType deck)
    {
        super(message, deck, new collectionCardHandler());
        collectionAmount = amount;
    }

}