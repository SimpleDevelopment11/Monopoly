package Cards;

import gameSpaces.boardSpace;

public class advanceToCard extends Card {

    public boardSpace advanceToSpace;
    public boolean toJailAdvance;

    public advanceToCard(boardSpace advancer, boolean toJail, String message, cardDeckType deck)
    {
        super(message, deck);
        advanceToSpace = advancer;
        toJailAdvance = toJail;
    }

}