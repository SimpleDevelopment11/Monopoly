package Cards;

public class collectionsFromPlayersCard extends Card {

    public int amountToCollect;

    public collectionsFromPlayersCard(int amount, String message, cardDeckType deck)
    {
        super(message, deck);
        amountToCollect = amount;
    }

}