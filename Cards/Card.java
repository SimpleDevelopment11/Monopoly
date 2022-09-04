package Cards;

public class Card implements java.io.Serializable {

    public String cardMessage;
    public cardDeckType belongingDeck;

    public Card(String message, cardDeckType deckType)
    {
        cardMessage = message;
        belongingDeck = deckType;
    }

    public enum cardDeckType {
        COMMUNITYCHEST,
        CHANCE;
    }

}