package Cards;

import serviceCardHandlers.getOutOfJailFreeCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class getOutOfJailFreeCard extends Card {

    public getOutOfJailFreeCard(String message, cardDeckType deck)
    {
        super(message, deck, new getOutOfJailFreeCardHandler());
    }

}