package Cards;

import serviceCardHandlers.serviceCardHandler;

public class Card {

    public String cardMessage;
    public cardDeckType belongingDeck;
    public serviceCardHandler handler;

    public Card(String message, cardDeckType deckType, serviceCardHandler handler)
    {
        cardMessage = message;
        belongingDeck = deckType;
        this.handler = handler;
    }

    public enum cardDeckType {
        COMMUNITYCHEST,
        CHANCE;
    }

}