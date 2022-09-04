package Cards;

public class collectionCard extends Card {

    public int collectionAmount;

    public collectionCard(String message, int amount, cardDeckType deck)
    {
        super(message, deck);
        collectionAmount = amount;
    }

}