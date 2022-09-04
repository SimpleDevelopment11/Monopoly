package Cards;

public class paymentsToPlayersCard extends Card {

    public int amountToPay;

    public paymentsToPlayersCard(int amount, String message, cardDeckType deck)
    {
        super(message, deck);
        amountToPay = amount;
    }

}