package Cards;

import gameSpaces.boardSpace;
import serviceCardHandlers.advanceToCardHandler;
import serviceCardHandlers.serviceCardHandler;

public class advanceToCard extends Card {

    public boardSpace advanceToSpace;
    public boolean toJailAdvance;

    public advanceToCard(boardSpace advancer, boolean toJail, String message, cardDeckType deck)
    {
        super(message, deck, new advanceToCardHandler());
        advanceToSpace = advancer;
        toJailAdvance = toJail;
    }

}